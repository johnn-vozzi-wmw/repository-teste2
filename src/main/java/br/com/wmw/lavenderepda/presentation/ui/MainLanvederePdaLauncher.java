package br.com.wmw.lavenderepda.presentation.ui;

import totalcross.TotalCrossApplication;
import totalcross.io.IOException;

public class MainLanvederePdaLauncher {
	
	public static void main(String ...args) throws IOException, InterruptedException {
		 TotalCrossApplication.run(
			        MainLavenderePda.class
			        , "/r"
			        , "54434C42517E5FEB2B11D6C4"
			        , "/pos"
			        , "900,200"
			        , "/scr"
			        , "480x870"
			        , "/dataPath"
			        , "D:/wmw/ferramentas/sqlite-dbs/vendasapp1"
			        , "/cmdLine"
			        , "wdebug"
				 );
	}

}