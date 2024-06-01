package br.com.wmw.lavenderepda.business.domain;

public class StatusItemPedido extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPSTATUSITEMPEDIDO";

	public String cdEmpresa;
	public String cdStatusItemPedido;
	public String dsStatusItemPedido;
	public String cdStatusItemPedidoErp;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdStatusItemPedido);
		return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdStatusItemPedido;
	}

	@Override
	public String getDsDomain() {
		return dsStatusItemPedido;
	}
}
