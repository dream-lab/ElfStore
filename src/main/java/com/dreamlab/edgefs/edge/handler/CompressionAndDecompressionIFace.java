package com.dreamlab.edgefs.edge.handler;

public interface CompressionAndDecompressionIFace{
  // caw => compress and write
  // dar => decompress and read
  void compressAndWriteSnappy(String filePath, byte[] mbDataInBytesArray);
  byte[] decompressAndReadSnappy(String filePath,long uncompSize);

  void compressAndWriteGzip(String filePath, byte[] mbDataInBytesArray);
  byte[] decompressAndReadGzip(String filePath,long uncompSize);

  void compressAndWriteNA(String filePath, byte[] mbDataInBytesArray);
  byte[] decompressAndReadNA(String filePath,long uncompSize);
}
