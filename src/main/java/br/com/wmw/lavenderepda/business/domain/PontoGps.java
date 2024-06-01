package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PontoGps extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPONTOGPS";

	public static boolean EMITE_ALERTA_GPS_DESLIGADO = false;
	public static boolean colectingGps = false;

    public String cdEmpresa;
    public String cdRepresentante;
	public Date dtColeta;
	public String hrColeta;
    public double vlLatitude;
    public double vlLongitude;
    public double vlVelocidade;
    public String flStatus;
    public double vlPrecisao;
    //Nao persistentes
    public int nuLinhasEnvioServidor;
    public Vector rowKeysEnvioServidor = new Vector();

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PontoGps) {
            PontoGps pontogps = (PontoGps) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pontogps.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, pontogps.cdRepresentante) &&
                ValueUtil.valueEquals(dtColeta, pontogps.dtColeta) &&
                ValueUtil.valueEquals(hrColeta, pontogps.hrColeta);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(dtColeta);
        primaryKey.append(";");
        primaryKey.append(hrColeta);
        return primaryKey.toString();
    }


	public String toString() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(dtColeta);
		primaryKey.append(";");
		primaryKey.append(hrColeta);
		primaryKey.append(";");
		primaryKey.append(vlLatitude);
		primaryKey.append(";");
		primaryKey.append(vlLongitude);
		primaryKey.append(";");
		primaryKey.append(vlVelocidade);
		primaryKey.append(";");
		primaryKey.append(vlPrecisao);
		primaryKey.append(";");
		primaryKey.append(flStatus);
		primaryKey.append(";");
		primaryKey.append(flTipoAlteracao);
		primaryKey.append(";");
		primaryKey.append(cdUsuario);
		return primaryKey.toString();
	}
	
	public boolean isStopped() {
		return GpsData.FLSTATUS_STOPPED.equals(flStatus);
	}
	
	public boolean isSuccess() {
		return GpsData.FLSTATUS_SUCCESS.equals(flStatus);
	}
	
}