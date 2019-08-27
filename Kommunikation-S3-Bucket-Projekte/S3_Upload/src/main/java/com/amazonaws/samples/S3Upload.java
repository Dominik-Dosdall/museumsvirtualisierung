package com.amazonaws.samples;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * <p>
 * Fill in your AWS access credentials in the provided credentials file
 * template, and be sure to move the file to the default location
 * (C:\\Users\\user\\.aws\\credentials) where the sample code will load the credentials from.
 * <p>
 * <b>WARNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 *
 * http://aws.amazon.com/security-credentials
 */
public class S3Upload{

    public static void main(String[] args) throws IOException {
    	
    	S3Upload s3 = new S3Upload();
    }
    
    S3Upload() {
    	
    	
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\annal\\.aws\\credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\<user>\\.aws\\credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("eu-central-1")
            .build();

        String[] configInfos = checkForConfig();
        
        //Exit if config infos are bad
        if(configInfos == null) {
        	System.exit(1);
        }
        
        String path = configInfos[0];
        String bucketName = configInfos[1];
        String projectName = configInfos[2];
        String naming = configInfos[3];
        int objectCount = 0;
        int pictures = 0;
        try {
        	objectCount = Integer.parseInt(configInfos[4]);
        	pictures = Integer.parseInt(configInfos[5]);
        }catch(NumberFormatException e) {
        	JOptionPane.showMessageDialog(null,"Error at reading the object/pic count.\r\nPlease check your config-file.","Invalid number format",JOptionPane.INFORMATION_MESSAGE);
        	System.exit(2);
        }
        
        if(objectCount < 1 || pictures < 0) {
        	JOptionPane.showMessageDialog(null,"Object or pic count can't be below 1.","Invalid number format",JOptionPane.INFORMATION_MESSAGE);
        	System.exit(3);
        }
      
        try {
            /*
             * Create a new S3 bucket - Amazon S3 bucket names are globally unique,
             * so once a bucket name has been taken by any user, you can't create
             * another bucket with that same name.
             *
             * You can optionally specify a location for your bucket if you want to
             * keep your data closer to your applications or users.
             */
        	
        	//Check if Bucket exists
        	List<Bucket> buckets = s3.listBuckets();
        	boolean vorhanden = false;
        	for(Bucket b : buckets) {
        		if(b.getName().equals(bucketName)) {
        			vorhanden = true;
        		}
        	}
        	
        	//If no Bucket exists create one
        	if(!vorhanden) {
        		s3.createBucket(bucketName);
        	}
        	
        	
        	File dir = new File(path);
	        if(dir.exists()) {
	        	
	        	String pfad = path+"/"+projectName;
	        	
	        	for(int i = 0; i < objectCount; i++) {
	        		for(int j = 0; j < pictures; j++) {
	        			System.out.println("Uploading picture "+j+" of object "+i+"\n");
	        			String filename = naming+j+".jpeg";
	        			s3.putObject(new PutObjectRequest(bucketName, projectName+"/"+"obj_"+i+"/"+filename, new File("/"+pfad+"/obj_"+i+"/"+naming+j+".jpeg")));
	        		}
	        	}
	        	
	        	JOptionPane.showMessageDialog(null,"Upload successful!","Success!",JOptionPane.INFORMATION_MESSAGE);
	        }
	        else {
	        	JOptionPane.showMessageDialog(null,"Workspace not found!","Error",JOptionPane.ERROR_MESSAGE);
	        }
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private String[] checkForConfig() {  	
    	
        File config = new File("config_museum.txt");
    	String[] infos = null;
        //Create new config file
        if(!config.exists()) {
        	int option = JOptionPane.showConfirmDialog(null,"There is no config file in your directory.\r\nDo you wish to create one?","Config-Error",JOptionPane.YES_NO_OPTION);
        	if(option == 0) {
	        	try {
	                String configtext = "Workspace-Directory=\r\nBucketName=\r\nProjectName=\r\nPictureNaming=\r\nObjectCount=\r\nPicture-per-object=";
					FileOutputStream fout = new FileOutputStream("config_museum.txt");
					DataOutputStream dout = new DataOutputStream(fout);
					dout.writeBytes(configtext);
					dout.close();
					fout.close();
					JOptionPane.showMessageDialog(null,"Createt a new config file in the directory your .jar-file is.\r\nNow fill it and restart the program!","Config-Created",JOptionPane.INFORMATION_MESSAGE);
					return null;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}else {
        		return null;
        	}
        }else {
        	try {
				FileInputStream fin = new FileInputStream("config_museum.txt");
				DataInputStream din = new DataInputStream(fin);
				
				byte[] allBytes = din.readAllBytes();
				String[] allInfo = new String(allBytes).split("\r\n");
				String ws = allInfo[0].split("=")[1];
				String bucketName = allInfo[1].split("=")[1];
				String project = allInfo[2].split("=")[1];
				String naming = allInfo[3].split("=")[1];
				String objects = allInfo[4].split("=")[1];
				String pics = allInfo[5].split("=")[1];
				
				infos = new String[6];
				
				infos[0] = ws;
				infos[1] = bucketName;
				infos[2] = project;
				infos[3] = naming;
				infos[4] = objects;
				infos[5] = pics;
				
				din.close();
				fin.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null,"Error at reading your config file.\r\nIt is located in the same directory as your jar is.","Config-Fehler",JOptionPane.ERROR_MESSAGE);
				return null;
			}
        }
        return infos;
	}

}
