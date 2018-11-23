import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Test;

import java.nio.ByteBuffer;

public class TestCrypto {

    @Test
    public void testLengthEncoding(){
        byte[] header = new byte[]{(byte) 0x7f, (byte) 0xFF};
        int  len = ((header[0] & 0xff) << 8) | (header[1] & 0xff);
        System.out.println("Packet len: "+ len);
    }

    @Test
    public void testAuthCrypt(){

        byte[] bytes = ByteBuffer.allocate(4).putInt(256).array();
        System.out.println(Hex.encodeHexString(bytes));

        System.out.println("Target \t cc7b");
        byte [] key = new byte[0];

        try {
            // key aus DB
            key = Hex.decodeHex("90AB4EEEB65EC7526D0707D84F8CD2CFD8343C77CC4C134EED44178395CA8CB1A3B23AD508DE81F6");

            ArrayUtils.reverse(key);

            byte[] data = Hex.decodeHex("0004"); // should encrypt to ddc6
            doStuff(key,data);


        } catch (DecoderException e) {
            e.printStackTrace();
        }

    }


    public void doStuff(byte[] key, byte[] data){
        byte [] s1 = new byte[0];
        byte [] s2 = new byte[0];

        try {
            s1 = Hex.decodeHex("C2B3723CC6AED9B5343C53EE2F4367CE");
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        HMac enc_mac = new HMac(new SHA1Digest());
        enc_mac.init(new KeyParameter(s1));
        enc_mac.update(key,0,key.length);


        byte[] enc_session_key = new byte[enc_mac.getMacSize()];
        enc_mac.doFinal(enc_session_key,0);

        StreamCipher enc = new RC4Engine();
        enc.init(true, new KeyParameter(enc_session_key));

        /*
        byte [] padding = new byte[1024];
        byte [] out = new byte[1024];
        enc.processBytes(padding,0,padding.length,out,0);
        */

        for(int i = 0 ; i< 1024;i++){
            enc.returnByte((byte) 1);
        }




        byte[] data_out = new byte[4];
        enc.processBytes(data,0,data.length,data_out,0);
        System.out.println("res \t " + Hex.encodeHexString(data_out));
        //dec.processBytes(data,0,data.length,data_out,0);
        //System.out.println("data_out: " + Hex.encodeHexString(data_out));
    }
}
