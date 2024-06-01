package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class StatusPedidoPda extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPSTATUSPEDIDOPDA";

    public String cdStatusPedido;
    public String dsStatusPedido;
    public String flCreditoClienteAberto;
    public String flUsaWorkflow;
    public int nuOrdemWorkflow;
    public byte[] imIconeWorkflow;
    public String flRelacionaPedido;
    public String flComplementavel;
    public String flConsideraVolumeVendaMensal;
    public int cdCorFundo;
	public String flRelacionaPedidoPda;
	public String flIgnoraHistoricoItem;
	public String flConverteTipoPedidoReplicacao;
	public String flOcultaValoresComissao;
	public String flConsideraDescProgVlMin;
    public String flConsideraDescProgVlMax;
    
    public String flConsideraFamilia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusPedidoPda) {
            StatusPedidoPda statusPedido = (StatusPedidoPda) obj;
            return
                ValueUtil.valueEquals(cdStatusPedido, statusPedido.cdStatusPedido);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return cdStatusPedido;
    }

	public String getCdDomain() {
		return cdStatusPedido;
	}

	public String getDsDomain() {
		return dsStatusPedido;
	}
	
	@Override
	public String getSortStringValue() {
		return dsStatusPedido;
	}

	//@Override
	public int getSortIntValue() {
		return ValueUtil.getIntegerValue(cdStatusPedido);
	}

}
