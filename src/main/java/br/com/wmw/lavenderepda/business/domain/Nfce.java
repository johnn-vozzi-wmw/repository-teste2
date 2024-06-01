package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfceDTO;
import br.com.wmw.lavenderepda.business.domain.dto.NfceDTO;
import br.com.wmw.lavenderepda.business.service.ItemNfceService;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Nfce extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPNFCE";
	
	public static final String NFCE = "1";
	public static final String NFCE_CONTINGENCIA = "9";
	public static final int MAX_CARACTERES_QRCODE = 4296;
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdTipoEmissao;
    public double qtTotalItem;
    public double vlTotalNfce;
    public double vlTotalDesconto;
    public double vlTotalLiquidoNfce;
    public String dsFormaPagamento;
    public double vlTotalPago;
    public double vlTroco;
    public String nuChaveAcesso;
    public String nuNfce;
    public String nuSerie;
    public Date dtEmissao;
    public String nuVersaoQrCode;
    public String nuAmbienteNfce;
    public String dsDigestValueNfce;
    public String cdIdentificacaoCsc;
    public String dsUrlQrCode;
    public String hrEmissao;
    public String nuProtocoloAutorizacao;
    public Date dtAutorizacao;
    public String hrAutorizacao;
    public double vlTotalTributos;
    public double vlPctTributosFederais;
    public double vlPctTributosEstaduais;
    public double vlPctTributosMunicipais;
    public String cdMensagemRetorno;
	public String dsMensagemRetorno;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public Vector listItensNfce;
    public String[] cdRepresentanteSupList;
    
    public Nfce() {}
    
    public Nfce(NfceDTO nfceDTO) {
    	try {
    		if (nfceDTO.itemNfceList != null) {
    			listItensNfce = new Vector(nfceDTO.itemNfceList.length);
    			for (ItemNfceDTO itemNfceDTO : nfceDTO.itemNfceList) {
					listItensNfce.addElement(new ItemNfce(itemNfceDTO));
				}
    		}
    		FieldMapper.copy(nfceDTO, this);
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Nfce) {
            Nfce nfce = (Nfce) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, nfce.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, nfce.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, nfce.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, nfce.flOrigemPedido);
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
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        return primaryKey.toString();
    }

	public Vector getListItensNfce() throws SQLException {
		if ((!ValueUtil.isEmpty(getPrimaryKey())) && ((this.listItensNfce == null))) {
			ItemNfce itemNfce = new ItemNfce();
			itemNfce.cdEmpresa = cdEmpresa;
			itemNfce.cdRepresentante = cdRepresentante;
			itemNfce.nuPedido = nuPedido;
			itemNfce.flOrigemPedido = flOrigemPedido;
			
			this.listItensNfce = ItemNfceService.getInstance().findAllByExample(itemNfce);
		}
		
		return this.listItensNfce;
	}
	
	public boolean isNfceContingencia() {
		return NFCE_CONTINGENCIA.equals(cdTipoEmissao);
	}
	
	public boolean isNfce() {
		return NFCE.equals(cdTipoEmissao);
	}

}