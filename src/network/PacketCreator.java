package network;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class PacketCreator {

	/* Flags and sizes */
	public static int HEADER_SIZE = 8;
	public static int MAX_PACKETS = 255;
	public static int SESSION_START = 128;
	public static int SESSION_END = 64;
	public static int DATAGRAM_MAX_SIZE = 65507 - HEADER_SIZE;
	public static int MAX_SESSION_NUMBER = 255;

	public static String OUTPUT_FORMAT = "jpg"; 

	public static byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, format, baos);
		return baos.toByteArray();
	}

	/**
	 * Il metodo genera tutti i pacchetti UDP a partire da un immagine e da un sessionNumber
	 * L'immagine in input deve essere al max 16MB
	 * 
	 * @param image (MAX 16MB)
	 * @param sessionNumber
	 * @return
	 * @throws IOException
	 */
	public static List<byte[]> createPackets(BufferedImage image, int sessionNumber) throws IOException{
		byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);
		int packets = (int) Math.ceil(imageByteArray.length / (float)DATAGRAM_MAX_SIZE);
		List<byte[]> packetsList = new ArrayList<byte[]>();
		for(int i = 0; i <= packets; i++) {
			int flags = 0;
			flags = i == 0 ? flags | SESSION_START : flags;
			flags = (i + 1) * DATAGRAM_MAX_SIZE > imageByteArray.length ? flags | SESSION_END : flags;

			int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_MAX_SIZE : imageByteArray.length - i * DATAGRAM_MAX_SIZE;

			byte[] data = new byte[HEADER_SIZE + size];
			data[0] = (byte)flags;
			data[1] = (byte)sessionNumber;
			data[2] = (byte)packets;
			data[3] = (byte)(DATAGRAM_MAX_SIZE >> 8);
			data[4] = (byte)DATAGRAM_MAX_SIZE;
			data[5] = (byte)i;
			data[6] = (byte)(size >> 8);
			data[7] = (byte)size;

			System.arraycopy(imageByteArray, i * DATAGRAM_MAX_SIZE, data, HEADER_SIZE, size);
			packetsList.add(data);
			if((flags & SESSION_END) == SESSION_END) break;
		}
		return packetsList;
	}

}
