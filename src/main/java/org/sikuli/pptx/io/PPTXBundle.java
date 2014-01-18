package org.sikuli.pptx.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.io.Files;

public class PPTXBundle {

	static Logger logger = LoggerFactory.getLogger(PPTXBundle.class);

	// the root directory of the decompressed bundle on the file system
	private File rootDir;		

	// number of slides 
	private int slideCount = 0;

	public String toString(){
		return Objects.toStringHelper(this)
				.add("count", slideCount)
				.add("rootDir", rootDir).toString();
	}


	public static PPTXBundle createFrom(InputStream is) throws IOException{
		PPTXBundle bundle = new PPTXBundle(is);
		return bundle;
	}

	// takes pptxFile as the input input and tmpDir to unzip the file to a temporary directory
	PPTXBundle(InputStream is){
		rootDir = Files.createTempDir();
		doUnZipFile(is, rootDir);
		logger.trace("checking validity");
		if (isValid()){						
			slideCount = getSlidesDirectory().list().length - 1;
			logger.trace("valid: {} slides found", slideCount);
		}else{
			slideCount = 0;
			logger.trace("not valid");
		}		
	}
	
	public boolean isValid(){
		return rootDir.exists() && getSlidesDirectory().exists() && getRelationshipsDirectory().exists();
	}

	public int getSlideCount(){
		return slideCount;
	}

	private File getRelationshipsDirectory(){
		return new File(rootDir, "ppt" + File.separator + "slides" + File.separator + "_rels");
	}

	private File getSlidesDirectory(){
		return new File(rootDir, "ppt" + File.separator + "slides");
	}

	// Get the file of the slide rel for a slide number
	public File getSlideXMLRel(int slideNumber){
		String filename = String.format("slide%d.xml.rels", slideNumber);
		return new File(getRelationshipsDirectory(), filename);
	}		

	public File getSlideXML(int slideNumber){
		String filename = String.format("slide%d.xml", slideNumber);
		return new File(getSlidesDirectory(), filename);
	}

	static void doUnZipFile(InputStream is, File dest){
		logger.trace("unzipping to " + dest);
		byte[] buffer = new byte[1024];
		try{

			//create output directory
			File folder = dest;
			// if the directory doesn't exist, create it
			if(!folder.exists()){
				folder.mkdir();
			}
			// if the directory already exists, delete it and recreate it.
			else{
				FileUtils.deleteDirectory(folder);
				folder.mkdir();
			}

			// Next, unzip it.
			//get the zip file content
			ZipInputStream zis = new ZipInputStream(is);
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){

				String fileName = ze.getName();
				File newFile = new File(folder + File.separator + fileName);

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);             

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();   
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
		}
		catch(IOException ex){
			ex.printStackTrace(); 
		}
	}

}