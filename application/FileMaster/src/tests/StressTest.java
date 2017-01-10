package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONObject;
import org.junit.Test;
import utils.JSONUtils.JsonUtils;

public class StressTest {

	public static final int NUMBER_OF_UPLOAD_REQUESTS = 5;
	
	
	public static byte testUploadImagesResult;
	public static int uploadedImages;
	@Test
	public void testUploadImages() {
		
		testUploadImagesResult = 0;
		uploadedImages = 0;
		Thread [] threads = new Thread[NUMBER_OF_UPLOAD_REQUESTS];
		for(int i = 0; i < NUMBER_OF_UPLOAD_REQUESTS; ++i)
		{
			threads[i] = new Thread(new SendUploadRequestToSlaveRunnable("http://localhost:8080/FileMaster", "vladdima"));
		}
		for(int i = 0; i < NUMBER_OF_UPLOAD_REQUESTS; ++i)
		{
			threads[i].start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i < NUMBER_OF_UPLOAD_REQUESTS; ++i)
		{
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(testUploadImagesResult < 1) fail("Failed the test");
	}

}



class SendUploadRequestToSlaveRunnable implements Runnable
{
	//public static final String jsonDATA = "{\"data\":[\"https:\\/\\/farm6.staticflickr.com\\/5461\\/30594494954_a66f45f83b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5678\\/31046402710_d0d9ae04e0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5637\\/30770903495_5bb7ef4319.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5495\\/31046305420_f9d6ab6a3d.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5483\\/31046304130_2010fe5d63.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5638\\/31379607596_ffaf4172c3.jpg\",\"https:\\/\\/farm9.staticflickr.com\\/8296\\/7939746026_df9338c32e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5676\\/30594218034_fea7a62854.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5594\\/31300765161_290356592f.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5721\\/31415373805_5fc51eacb6.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5812\\/30594121504_cdf49d9364.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5488\\/30594120734_4fa3e599b7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5569\\/30594119474_72a30cb0c3.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5583\\/30608244533_78e39c84d7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5503\\/30608243873_2f340f36e8.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5476\\/30608242683_b8a62664e9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5343\\/30608242073_4d8880548b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5584\\/30608241143_123a727168.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5731\\/30608240283_ec1057076c.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5607\\/30608238273_fb55e5b758.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5685\\/30608237303_8edd09f262.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5522\\/30608236563_e765664a38.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5798\\/30608235893_2953de25ab.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5507\\/30608235063_77b6856088.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5746\\/31415334065_ef6d0a9254.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5753\\/31415332605_7da5eaa328.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5463\\/31415331605_3c7fbef5b2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5501\\/31415330555_74a4820281.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5798\\/31300706381_eda0518b6a.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5593\\/31300705691_d61fab63db.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5589\\/31300705031_d4235fa7d9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5516\\/31415280255_ed522b8811.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5717\\/30594061624_fd3ebc66e0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5683\\/31045987570_da06d82ae0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5743\\/31045990740_92e51e6c2d.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5327\\/31379280446_9a3612c12e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5688\\/30608116323_4c04199187.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5511\\/31270517062_d1f5352df5.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5728\\/31045864060_bcaff6bce3.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5753\\/31045837770_db424aca93.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5537\\/31270418952_7d2e7734e8.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5827\\/31270407452_8f5e46c1c0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5758\\/31045805590_9327685a11.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5558\\/30593781504_fae7aba66e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5520\\/31414885865_3c452c9284.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5535\\/31414825275_7f20a1837f.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5758\\/31264264922_954042d923.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5727\\/30593758104_d9e72e0026.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5576\\/31270251692_1ac2c6a757.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5465\\/31414892455_ba1847422b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5501\\/31378983596_a76c9f35b2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5778\\/30607779843_052fa192d1.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5590\\/31045490340_e6510cf9af.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5571\\/31414730265_f5a0975880.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5710\\/31414728005_60a88695a9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5686\\/30593477144_e43ab5f972.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5515\\/30593473764_1186397144.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5661\\/31378783156_cd28e1cb69.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5596\\/31378781296_3451b80eb2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5663\\/30607623053_999eae79c5.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5693\\/31414566725_09b3022546.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5639\\/31299836631_361d262916.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5718\\/30593166784_81987f284f.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5585\\/31378475726_48382bacd1.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5725\\/31045168280_919465d97b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5731\\/31299760891_25d70f2df6.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5732\\/30593141334_bf0fd55bd7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5332\\/31269713652_dcd617ec47.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5346\\/30607235813_ed0096b7d7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5497\\/31378333026_88661b1aab.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5329\\/31299572481_7f437f1bc2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5720\\/30593021254_5c912e35dd.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5506\\/31269592472_99612bc6d4.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5589\\/31378284606_5fa27721d4.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5773\\/31414178225_7d54b505bf.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5580\\/31269544192_44dbd174d4.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5516\\/30607114313_d7f401ec50.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5698\\/31414143295_749fbc0dcb.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5788\\/30592965554_4cfefa319c.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5807\\/31414113835_af36d7e914.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5782\\/30607091383_8c0faf629a.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5683\\/31044911700_6bc5f57dee.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5583\\/31299440481_0b4c1c8a88.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5442\\/31269459212_41125df246.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5540\\/31414055985_c4213f8ae3.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5556\\/31044841310_6d364d26c7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5514\\/31299339461_663cda9eeb.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5477\\/30606977673_c601069252.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5513\\/31269341262_3b562112f8.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5771\\/31044988240_1b7eeb81b9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5457\\/30607072883_23db25855f.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5575\\/30606493143_994c4696bd.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5737\\/31299312311_a22181be70.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5643\\/31378057926_ab7a31db05.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5576\\/30606905103_db954086df.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5479\\/30606874153_e469394401.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5331\\/31299249661_38f5da61a2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5613\\/31044747780_ff2508e304.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5452\\/31378015226_a4162f4585.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5452\\/30592751394_d7970b6cd5.jpg\"]}";
	public static final String jsonDATA = "{\"data\":[\"https:\\/\\/farm6.staticflickr.com\\/5461\\/30594494954_a66f45f83b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5678\\/31046402710_d0d9ae04e0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5637\\/30770903495_5bb7ef4319.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5495\\/31046305420_f9d6ab6a3d.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5483\\/31046304130_2010fe5d63.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5638\\/31379607596_ffaf4172c3.jpg\",\"https:\\/\\/farm9.staticflickr.com\\/8296\\/7939746026_df9338c32e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5676\\/30594218034_fea7a62854.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5594\\/31300765161_290356592f.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5721\\/31415373805_5fc51eacb6.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5812\\/30594121504_cdf49d9364.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5488\\/30594120734_4fa3e599b7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5569\\/30594119474_72a30cb0c3.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5583\\/30608244533_78e39c84d7.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5503\\/30608243873_2f340f36e8.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5476\\/30608242683_b8a62664e9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5343\\/30608242073_4d8880548b.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5584\\/30608241143_123a727168.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5731\\/30608240283_ec1057076c.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5607\\/30608238273_fb55e5b758.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5685\\/30608237303_8edd09f262.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5522\\/30608236563_e765664a38.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5798\\/30608235893_2953de25ab.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5507\\/30608235063_77b6856088.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5746\\/31415334065_ef6d0a9254.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5753\\/31415332605_7da5eaa328.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5463\\/31415331605_3c7fbef5b2.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5501\\/31415330555_74a4820281.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5798\\/31300706381_eda0518b6a.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5593\\/31300705691_d61fab63db.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5589\\/31300705031_d4235fa7d9.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5516\\/31415280255_ed522b8811.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5717\\/30594061624_fd3ebc66e0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5683\\/31045987570_da06d82ae0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5743\\/31045990740_92e51e6c2d.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5327\\/31379280446_9a3612c12e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5688\\/30608116323_4c04199187.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5511\\/31270517062_d1f5352df5.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5728\\/31045864060_bcaff6bce3.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5753\\/31045837770_db424aca93.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5537\\/31270418952_7d2e7734e8.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5827\\/31270407452_8f5e46c1c0.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5758\\/31045805590_9327685a11.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5558\\/30593781504_fae7aba66e.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5520\\/31414885865_3c452c9284.jpg\",\"https:\\/\\/farm6.staticflickr.com\\/5535\\/31414825275_7f20a1837f.jpg\"]}";
	
	public String link;
	public String token;
	
	public SendUploadRequestToSlaveRunnable(String link, String token)
	{
		this.link = link;
		this.token = token;
	}

	@Override
	public void run() {
		try {
            URL url = new URL(this.link + "/upload?key=" + this.token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setConnectTimeout(3);
            OutputStreamWriter write = new OutputStreamWriter(conn.getOutputStream());
            write.write(jsonDATA);
            write.flush();
            
            JSONObject responseJSON = JsonUtils.readBody(new InputStreamReader(conn.getInputStream()));
            //FIXME: increment successfullyUploadedImages with the number returned;
            if(responseJSON != null)System.out.println(responseJSON.toJSONString());
            else System.out.println("[TEST] INVALID RESPONSE JSON BODY");
        } catch (MalformedURLException ex) {
        	// Should never enter here
        } catch (IOException ex) {
        	//FIXME: 40x Error, because connection timeout => fail test;
        	StressTest.testUploadImagesResult = -1;
        }
	}
	
}