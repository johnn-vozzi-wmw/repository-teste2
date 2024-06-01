package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VariavelCalculo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVARIAVELCALCULO";
	public static final String ENTIDADE_EMPRESA = "empresa";
	public static final String ENTIDADE_CLIENTE = "cliente";
	public static final String ENTIDADE_PRODUTO = "produto";
	public static final String TIPO_NUMERO = "1";
	public static final String TIPO_TEXTO = "2";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdEntidade;
    public String pkEntidade;
    public String cdVariavel;
    public String dsVariavel;
    public String dsValorVariavel;
    public String cdTipoValorVariavel;
    public String cdCliente;
    public String cdProduto;


    public VariavelCalculo(String cdEmpresa, String cdRepresentante, String cdEntidade, String cdCliente, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdEntidade = cdEntidade;
		this.cdCliente = cdCliente;
		this.cdProduto = cdProduto;
	}
    
    
    public VariavelCalculo() {	}
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariavelCalculo) {
            VariavelCalculo variavelCalculo = (VariavelCalculo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, variavelCalculo.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, variavelCalculo.cdRepresentante) && 
                ValueUtil.valueEquals(cdEntidade, variavelCalculo.cdEntidade) && 
                ValueUtil.valueEquals(pkEntidade, variavelCalculo.pkEntidade) && 
                ValueUtil.valueEquals(cdVariavel, variavelCalculo.cdVariavel);
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
        primaryKey.append(cdEntidade);
        primaryKey.append(";");
        primaryKey.append(pkEntidade);
        primaryKey.append(";");
        primaryKey.append(cdVariavel);
        return primaryKey.toString();
    }
    
    public Object getValorVariavelCalculo() {
    	if (cdTipoValorVariavel == null) return dsValorVariavel;
    	
    	switch (cdTipoValorVariavel) {
		case TIPO_NUMERO:
			return ValueUtil.getDoubleValue(dsValorVariavel);
			
		default:
			return dsValorVariavel;
		}
	}

}