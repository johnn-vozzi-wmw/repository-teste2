package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.util.Util;

public class ProdutoUnidade extends LavendereBaseDomain {

    public static final String TABLE_NAME = "TBLVPPRODUTOUNIDADE";
    public static final String CDUNIDADE_PADRAO = "0";
    public static final String DS_COLUNA_NUCONVERSAOUNIDADE = "NUCONVERSAOUNIDADE";

	public static final String FL_MULTIPLICA = "M";
	public static final String FL_DIVIDE = "D";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdUnidade;
    public String cdProduto;
    public String cdItemGrade1;
    public double nuConversaoUnidade;
    public double vlIndiceFinanceiro;
    public String flDivideMultiplica;
    public String nuCodigoBarras;
    public int nuMultiploEspecial;
    
    // Não persistente
    public Unidade unidade;
    public Unidade unidadeFracao;
    public int nuFracao;
    public double vlEmbElementar;
	public double qtEmbElementar;

	public ProdutoUnidade() { }

	public ProdutoUnidade(ItemPedido itemPedido) {
		this.cdEmpresa = itemPedido.cdEmpresa;
		this.cdRepresentante = itemPedido.cdRepresentante;
		this.cdUnidade = itemPedido.cdUnidade;
		this.cdProduto = itemPedido.cdProduto;
		this.cdItemGrade1 = itemPedido.cdItemGrade1;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoUnidade) {
            ProdutoUnidade produtounidade = (ProdutoUnidade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtounidade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtounidade.cdRepresentante) &&
                ValueUtil.valueEquals(cdUnidade, produtounidade.cdUnidade) &&
                ValueUtil.valueEquals(cdProduto, produtounidade.cdProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, produtounidade.cdItemGrade1);
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
        primaryKey.append(cdUnidade);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade1);
        return primaryKey.toString();
    }

	public String getCdDomain() {
		return cdUnidade;
	}

	public String getDsDomain() {
		if ((unidade != null) && ValueUtil.isNotEmpty(unidade.dsUnidade)) {
    		return unidade.dsUnidade;
    	} else {
    		return cdUnidade;
    	}
	}
	
	@Override
	public double getSortDoubleValue() {
		return nuConversaoUnidade;
	}

	public boolean isMultiplica() {
		return FL_MULTIPLICA.equals(flDivideMultiplica);
	}
	
	@Override
	public String toString() {
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			String dsUnidade;
			nuFracao = nuFracao <= 0 ? 1 : nuFracao;
			if (nuFracao == 1 && nuConversaoUnidade == 1) {
				return getDsDomain();
			}
			double value;
			if (isMultiplica()) {
				value = nuConversaoUnidade * nuFracao;
			} else {
				value = nuFracao / nuConversaoUnidade;
			}
			if (value > 1) {
				dsUnidade = ValueUtil.isNotEmpty(unidadeFracao.dsUnidadePlural) ? unidadeFracao.dsUnidadePlural :  Messages.PRODUTO_DSUNIDADEPADRAO_PLURAL;
			} else {
				dsUnidade = ValueUtil.isNotEmpty(unidadeFracao.dsUnidade) ? unidadeFracao.dsUnidade :  Messages.PRODUTO_DSUNIDADEPADRAO;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(getDsDomain()).append(" ").append(Messages.MSG_COM).append(" ").append(Util.getIntOuDecimal(value)).append(" ").append(dsUnidade);
			return sb.toString();
		}
		return super.toString();
	}
}