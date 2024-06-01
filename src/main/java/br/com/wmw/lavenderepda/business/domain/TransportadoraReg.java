package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TransportadoraReg extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPTRANSPORTADORAREG";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTransportadora;
    public String cdRegiao;
    public String cdTipoFrete;
    public double vlMinPedido;
    public String flPossuiContrato;
    public double vlPctFrete;
    public double vlMinFrete;
    public double vlMaxPedido;
    //--
    public Transportadora transportadora;
    public TipoFrete tipoFrete;
    public Regiao regiao;
    public double vlFrete;
	public boolean ignoraVlMinMaxFrete;
	public Transportadora transportadoraFilter;
	public TipoFrete tipoFreteFilter;
	public Regiao regiaoFilter;
	public double vlTotalPedidoFilter;
	public boolean ordenaPorFreteCifVlFrete;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TransportadoraReg) {
            TransportadoraReg transportadoraReg = (TransportadoraReg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, transportadoraReg.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, transportadoraReg.cdRepresentante) && 
                ValueUtil.valueEquals(cdTransportadora, transportadoraReg.cdTransportadora) && 
                ValueUtil.valueEquals(cdRegiao, transportadoraReg.cdRegiao) && 
                ValueUtil.valueEquals(cdTipoFrete, transportadoraReg.cdTipoFrete) &&
                ValueUtil.valueEquals(vlMinPedido, transportadoraReg.vlMinPedido);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdTransportadora);
        primaryKey.append(";");
        primaryKey.append(cdRegiao);
        primaryKey.append(";");
        primaryKey.append(cdTipoFrete);
        primaryKey.append(";");
        primaryKey.append(vlMinPedido);
        primaryKey.append(";");
        return primaryKey.toString();
    }

	public boolean isPossuiContrato() {
		return ValueUtil.VALOR_SIM.equals(flPossuiContrato);
	}

	@Override
	public double getSortDoubleValue() {
		return vlFrete;
	}
	
	public void setRowkey(String rowKey) {
		String[] values = rowKey.split(";");	
		cdEmpresa = values[0];
		cdRepresentante = values[1];
		cdTransportadora = values[2];
		cdRegiao = values[3];
		cdTipoFrete = values[4];
		vlMinPedido = ValueUtil.getDoubleSimpleValue(values[5]);
	}
    
}