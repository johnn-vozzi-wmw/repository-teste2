package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class NaoVendaProdPedido extends BaseDomain {

    public static String TABLE_NAME = "TBLVPNAOVENDAPRODPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProduto;
    public String cdMotivo;
    public String dsJustificativa;
    public String hrAlteracao;
    public Date dtAlteracao;
    
    //Não Persistente
    private MotNaoVendaProduto motNaoVendaProduto;
    
    public NaoVendaProdPedido() {
	}

    public NaoVendaProdPedido(Pedido pedido, String cdProduto) {
		cdEmpresa = pedido.cdEmpresa;
		cdRepresentante = pedido.cdRepresentante;
		nuPedido = pedido.nuPedido;
		flOrigemPedido = pedido.flOrigemPedido;
		this.cdProduto = cdProduto;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof NaoVendaProdPedido) {
            NaoVendaProdPedido naoVendaProdPedido = (NaoVendaProdPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, naoVendaProdPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, naoVendaProdPedido.cdRepresentante) &&
            	ValueUtil.valueEquals(nuPedido, naoVendaProdPedido.nuPedido) &&
            	ValueUtil.valueEquals(flOrigemPedido, naoVendaProdPedido.flOrigemPedido) &&
            	ValueUtil.valueEquals(cdProduto, naoVendaProdPedido.cdProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
    }
    
    public MotNaoVendaProduto getMotNaoVendaProduto() {
		return motNaoVendaProduto;
	}
    
    public void setMotNaoVendaProduto(MotNaoVendaProduto motNaoVendaProduto) {
		this.motNaoVendaProduto = motNaoVendaProduto;
	}

    

}