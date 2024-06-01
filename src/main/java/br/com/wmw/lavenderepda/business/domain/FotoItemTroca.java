package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoItemTroca extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFOTOITEMTROCA";
	public static final String CLASS_SIMPLE_NAME = "FotoItemTroca";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public int cdFotoItemTroca;
	public String cdUsuarioCriacao;
	public String nmFoto;
	public int nuTamanho;
	public Date dtModificacao;
	public String flEnviadoServidor;
	
	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(cdEmpresa);
		strBuffer.append(";");
		strBuffer.append(cdRepresentante);
		strBuffer.append(";");
		strBuffer.append(flOrigemPedido);
		strBuffer.append(";");
		strBuffer.append(nuPedido);
		strBuffer.append(";");
		strBuffer.append(cdProduto);
		strBuffer.append(";");
		strBuffer.append(flTipoItemPedido);
		strBuffer.append(";");
		strBuffer.append(nuSeqProduto);
		strBuffer.append(";");
		strBuffer.append(cdFotoItemTroca);
		strBuffer.append(";");
		strBuffer.append(cdUsuarioCriacao);
		return strBuffer.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FotoItemTroca) {
			FotoItemTroca fotoItemTroca = (FotoItemTroca) obj;
			return
					ValueUtil.valueEquals(cdEmpresa, fotoItemTroca.cdEmpresa) &&
							ValueUtil.valueEquals(cdRepresentante, fotoItemTroca.cdRepresentante) &&
							ValueUtil.valueEquals(nuPedido, fotoItemTroca.nuPedido) &&
							ValueUtil.valueEquals(flOrigemPedido, fotoItemTroca.flOrigemPedido) &&
							ValueUtil.valueEquals(cdProduto, fotoItemTroca.cdProduto) &&
							ValueUtil.valueEquals(flTipoItemPedido, fotoItemTroca.flTipoItemPedido) &&
							ValueUtil.valueEquals(nuSeqProduto, fotoItemTroca.nuSeqProduto) &&
							ValueUtil.valueEquals(cdFotoItemTroca, fotoItemTroca.cdFotoItemTroca);
		}
		return false;
	}


	public static String getPathImg() {
		return FotoUtil.getPathImg(FotoItemTroca.class);
	}

	public boolean isFotoPedidoEnviadaServidor() {
		return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}
}
