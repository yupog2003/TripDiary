package com.yupog2003.tripdiary.data;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyFileBody extends FileBody {

	MyListener listener;
	MyOutputStream mos;

	public MyFileBody(File file) {
		super(file);
	}

	public MyFileBody(File file, ContentType contentType) {
		super(file, contentType);
	}

	public MyFileBody(File file, ContentType contentType, String filename) {
		super(file, contentType, filename);
	}

	public void setListener(MyListener listener) {
		this.listener = listener;
	}

	public static interface MyListener {
		public void progressChange(long progress);
	}

	@Override
	public void writeTo(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub
		super.writeTo(new MyOutputStream(arg0));
	}

	class MyOutputStream extends FilterOutputStream {

		long transferred;

		public MyOutputStream(OutputStream out) {
			super(out);
			// TODO Auto-generated constructor stub
			transferred = 0;
		}

		@Override
		public void write(byte[] buffer, int offset, int length) throws IOException {
			// TODO Auto-generated method stub
			out.write(buffer, offset, length);
			transferred += length;
			if (listener != null)
				listener.progressChange(transferred);
		}

		@Override
		public void write(int oneByte) throws IOException {
			// TODO Auto-generated method stub
			out.write(oneByte);
			transferred++;
			if (listener != null)
				listener.progressChange(transferred);

		}

	}
}
