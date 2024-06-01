package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.BigDecimal;
import totalcross.util.Date;

public class VenctoPagamentoPedido extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVENCTOPAGAMENTOPEDIDO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdPagamentoPedido;
	public int nuSeqVenctoPagamentoPedido;
	public Date dtVencimento;
	public double vlBoleto;
	
	//Nao persistente
	public BigDecimal nuDocumento;
	
	@Override
	public String getPrimaryKey() {
		StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdPagamentoPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuSeqVenctoPagamentoPedido);
        return strBuffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VenctoPagamentoPedido) {
			VenctoPagamentoPedido vencto = (VenctoPagamentoPedido) obj;
			return ValueUtil.valueEquals(cdEmpresa, vencto.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, vencto.cdRepresentante) &&
					ValueUtil.valueEquals(nuPedido, vencto.nuPedido) &&
					ValueUtil.valueEquals(flOrigemPedido, vencto.flOrigemPedido) &&
					ValueUtil.valueEquals(cdPagamentoPedido, vencto.cdPagamentoPedido) &&
					ValueUtil.valueEquals(nuSeqVenctoPagamentoPedido, vencto.nuSeqVenctoPagamentoPedido);
		}
		return false;
	}
	
}