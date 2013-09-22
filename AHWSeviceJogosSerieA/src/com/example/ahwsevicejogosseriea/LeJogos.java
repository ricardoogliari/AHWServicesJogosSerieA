package com.example.ahwsevicejogosseriea;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class LeJogos extends Service {

	private int id;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new TarefaAssincrona().execute();
			}
		};
		timer.schedule(tt, 2000, 2000);
		
		

		return 1;
	}
	
	class TarefaAssincrona extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = null;
			HttpURLConnection con = null;
			try {
				URL url = new URL(
						"http://192.168.1.101:8080/AHWServicesJogosSerieA/index.jsp");

				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");

				InputStream in = new BufferedInputStream(con.getInputStream());
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				return sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JOGOSSERVICE", ""+e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
						con.disconnect();
					} catch (IOException e) {
					}
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			JSONObject objeto;
			try {
				objeto = new JSONObject(result);
				String atualizacoes = objeto.getString("Atualizacoes");
				
				if (atualizacoes.equals("S")){
					JSONObject fato = objeto.getJSONObject("Fato");
					int idRecebido = fato.getInt("id");
					
					if (idRecebido != id){
						NotificationCompat.Builder mBuilder =
						        new NotificationCompat.Builder(getBaseContext())
						        .setSmallIcon(R.drawable.ic_launcher)
						        .setContentTitle(fato.getString("tipo") + " - " + fato.getString("nome"))
						        .setContentText(fato.getString("minuto") + "min do "+fato.getString("tempo") + "º tempo. Jogo: " +
						        		fato.getString("jogo"));
						// Creates an explicit intent for an Activity in your app
						//Intent resultIntent = new Intent(this, ResultActivity.class);

						// The stack builder object will contain an artificial back stack for the
						// started Activity.
						// This ensures that navigating backward from the Activity leads out of
						// your application to the Home screen.
						//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
						// Adds the back stack for the Intent (but not the Intent itself)
						//stackBuilder.addParentStack(ResultActivity.class);
						// Adds the Intent that starts the Activity to the top of the stack
						//stackBuilder.addNextIntent(resultIntent);
						//PendingIntent resultPendingIntent =
						        //stackBuilder.getPendingIntent(
						            //0,
						            //PendingIntent.FLAG_UPDATE_CURRENT
						        //);
						//mBuilder.setContentIntent(resultPendingIntent);
						NotificationManager mNotificationManager =
						    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						// mId allows you to update the notification later on.
						mNotificationManager.notify(1, mBuilder.build());
						id = idRecebido;
					}
				} else {
					Log.e("JOGOSERVICE", "------------------------");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}

}