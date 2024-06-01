package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class BoletoConfig extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPBOLETOCONFIG";
	public static final String NMBANCO_COLUMN_NAME = "NMBANCO";
	
	public static final String MODO_CEF = "1";
	public static final String MODO_BRADESCO = "2";
	public static final String MODO_SICREDI = "3";
	public static final String MODO_SANTANDER = "4";
	public static final String MODO_UNICRED = "5";
	public static final String MODO_CDBARRAS = "6";
	public static final String MODO_ITAU = "7";

    public String cdBoletoConfig;
    public String nmBanco;
    public byte[] imBanco;
    public String cdBeneficiario;
    public String nuAgenciaCodigoCedente;
    public String nuBanco;
    public String nuCarteira;
    public String flModoBoleto;
    public String nuAgencia;
    public String nuConta;
    public String dsLocalPagamento;
    public int nuByteBoleto;
    public String dsCarteira;
    public String dsEspecieBanco;
    public String dsMensagemSacador;
    
    public BoletoConfig() {
    	super(TABLE_NAME);
	}

	public BoletoConfig(String dsTabela) {
		super(dsTabela);
	}
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof BoletoConfig) {
            BoletoConfig boletoConfig = (BoletoConfig) obj;
            return ValueUtil.valueEquals(getPrimaryKey(), ValueUtil.isNotEmpty(boletoConfig.cdBoletoConfig) ? boletoConfig.cdBoletoConfig : boletoConfig.nuBanco);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return getCdDomain();
    }

	@Override
	public String getCdDomain() {
		return ValueUtil.isNotEmpty(cdBoletoConfig) ? cdBoletoConfig : nuBanco;
	}

	@Override
	public String getDsDomain() {
		return nmBanco;
	}

}