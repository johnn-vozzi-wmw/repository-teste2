package br.com.wmw.lavenderepda.business.domain;

import totalcross.util.Date;

public class CatalogoItemPedLog extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCATALOGOITEMPEDLOG";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCatalogoItemPedidoLog;
	public int nuSeqCatalogoItemPedidoLog;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public double vlItemPedido;
	public Date dtGeracaoCatalogo;
	public String hrGeracaoCatalogo;
	public String flTipoAlteracao;
	public Date dtAlteracao;
	public String hrAlteracao;

	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(cdEmpresa).append(";");
		strBuffer.append(cdRepresentante).append(";");
		strBuffer.append(cdCatalogoItemPedidoLog).append(";");
		strBuffer.append(nuSeqCatalogoItemPedidoLog);
		return strBuffer.toString();
	}

	@Override
	public String getCdDomain() {
		return cdCatalogoItemPedidoLog + nuSeqCatalogoItemPedidoLog;
	}

	@Override
	public String getDsDomain() {
		return cdCatalogoItemPedidoLog;
	}
}
