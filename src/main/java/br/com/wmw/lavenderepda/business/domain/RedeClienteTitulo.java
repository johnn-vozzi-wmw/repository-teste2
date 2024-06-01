package br.com.wmw.lavenderepda.business.domain;

public class RedeClienteTitulo extends TituloFinanceiro {

    public static String TABLE_NAME = "TBLVPREDECLIENTETITULO";
    
    public String dsTipoDocumento;
    public String dsPortador;
    public String dsStatus;
    public String cdRepresentanteTitulo;
    
    //-- Não persistente
    public Rede rede;
    public RedeCliente redeCliente;

    public RedeClienteTitulo() {
		super(TABLE_NAME);
	}

    public RedeClienteTitulo(String tableName) {
		super(tableName);
	}

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(nuNf);
    	strBuffer.append(";");
    	strBuffer.append(nuSerie);
    	strBuffer.append(";");
    	strBuffer.append(nuTitulo);
    	strBuffer.append(";");
    	strBuffer.append(nuSubDoc);
        return strBuffer.toString();
    }
    

}