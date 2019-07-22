package com.dreamlab.edgefs.edge.handler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

public class CompressionAndDecompression implements CompressionAndDecompressionIFace {
	private static final Logger LOGGERCND = LoggerFactory.getLogger(EdgeServiceHandler.class);
	
	@Override
	public void compressAndWriteGzip(String filePath, byte[] mbDataInBytesArray) {
		LOGGERCND.info("Starting compression : Gzip");
		filePath = filePath.concat(".gz");
		GzipCompressorOutputStream gzOut = null;
		try {
			Path newFilePath = Paths.get(filePath);
			OutputStream os = Files.newOutputStream(Files.createFile(newFilePath));
			BufferedOutputStream bos = new BufferedOutputStream(os);
			gzOut = new GzipCompressorOutputStream(bos);
			gzOut.write(mbDataInBytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGERCND.info("Write Done : Gzip");
	}

	@Override
	public byte[] decompressAndReadGzip(String filePath,long uncompSize) {
		LOGGERCND.info("Starting decompression : Gzip");
		filePath = filePath.concat(".gz");
		GzipCompressorInputStream gzIn = null;
		byte[] byteArray = null;
		try {
			Path targetFilePath = Paths.get(filePath);
			InputStream is = Files.newInputStream(targetFilePath);
			BufferedInputStream bis = new BufferedInputStream(is);
			gzIn = new GzipCompressorInputStream(bis);
			byteArray = new byte[(int)uncompSize];
			gzIn.read(byteArray);
			return byteArray;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGERCND.info("Read done : Gzip");
		return byteArray;
	}

	//for snappy, library from xerial has been used instead of apache commons compress.
	//The reason being during decompression using commons compress for snappy null values 
	//are obtained.
	@Override
	public void compressAndWriteSnappy(String filePath, byte[] mbDataInBytesArray) {
		LOGGERCND.info("Starting compression : Snappy");
		filePath = filePath.concat(".snappy");
		SnappyOutputStream snOut = null;
		try {
			Path newFilePath = Paths.get(filePath);
			OutputStream os = Files.newOutputStream(Files.createFile(newFilePath));
			BufferedOutputStream bos = new BufferedOutputStream(os);
			snOut = new SnappyOutputStream(bos);
			snOut.write(mbDataInBytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				snOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGERCND.info("Write Done : Snappy");
	}

	@Override
	public byte[] decompressAndReadSnappy(String filePath,long uncompSize) {
		LOGGERCND.info("Starting decompression : Snappy");
		filePath = filePath.concat(".snappy");
		SnappyInputStream snIn = null;
		byte[] byteArray = null;
		try {
			Path targetFilePath = Paths.get(filePath);
			InputStream is = Files.newInputStream(targetFilePath);
			BufferedInputStream bis = new BufferedInputStream(is);
			snIn = new SnappyInputStream(bis);
			byteArray = new byte[(int)uncompSize];
			snIn.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				snIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGERCND.info("Read Done : Snappy");
		return byteArray;		
	}

	@Override
	public void compressAndWriteNA(String filePath, byte[] mbDataInBytesArray) {
		LOGGERCND.info("Start write : NA");
		try {
			File myFile = new File(filePath);
			FileUtils.writeByteArrayToFile(myFile, mbDataInBytesArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGERCND.info("Write Done : NA");
	}

	@Override
	public byte[] decompressAndReadNA(String filePath,long uncompSize) {
		LOGGERCND.info("Start read : NA");
		byte[] byteArray = null;
		try {
			File mbFile = new File(filePath);
			byteArray = FileUtils.readFileToByteArray(mbFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGERCND.info("Read Done : NA");
		return byteArray;
	}

}
