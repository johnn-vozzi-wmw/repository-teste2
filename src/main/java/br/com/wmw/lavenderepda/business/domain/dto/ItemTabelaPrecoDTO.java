package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import totalcross.util.Date;

public class ItemTabelaPrecoDTO {

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdProduto;
	public String cdUf;
	public String cdItemGrade1;
	public String cdItemGrade2;
	public String cdItemGrade3;
	public String cdUnidade;
	public int qtItem;
	public int cdPrazoPagtoPreco;
	public int cdLinha;
	public double vlUnitario;
	public double vlUnitarioEspecial;
	private double vlPctMaxDesconto;
	public double vlPctMaxAcrescimo;
	public double vlMaxDesconto;
	public double vlMaxAcrescimo;
	public double vlPrecoEmbalagemPrimaria;
	public String flPromocao;
	public double vlBase;
	public double vlBaseEspecial;
	public double vlPctComissao;
	public double vlPctDescPromocional;
	public double vlPrecoFrete;
	public double vlReducaoOptanteSimples;
	public String flBloqueado;
	public double qtMaxVenda;
	public double vlPrecoEmbalagem;
	public double nuConversaoUnidade;
	public double vlOportunidade;
	public String flPrecoQueda;
	public String flUsaDesconto3;
	public double vlPctPrevisaRentabilidade;
	public double vlPctMargemRentabilidade;
	public int nuPromocao;
	public double vlDescontoPromo;
	public Date dtInicioPromocao;
	public int qtMinItensNormais;
	public double vlPctRentabilidadeEsp;
	public double vlPctRentabilidadeMin;
	public double vlPctMaxParticipacao;
	public double vlMinItemPedido;
	public double vlEmbalagemSt;
	
	public ItemTabelaPrecoDTO() {
		super();
	}

    public ItemTabelaPrecoDTO copy(final ItemTabelaPreco itemTabelaPreco) {
		try {
			FieldMapper.copy(itemTabelaPreco, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}

	public String getCdProduto() {
		return cdProduto;
	}

	public String getCdUf() {
		return cdUf;
	}

	public String getCdItemGrade1() {
		return cdItemGrade1;
	}

	public String getCdItemGrade2() {
		return cdItemGrade2;
	}

	public String getCdItemGrade3() {
		return cdItemGrade3;
	}

	public String getCdUnidade() {
		return cdUnidade;
	}

	public int getQtItem() {
		return qtItem;
	}

	public int getCdPrazoPagtoPreco() {
		return cdPrazoPagtoPreco;
	}

	public int getCdLinha() {
		return cdLinha;
	}

	public double getVlUnitario() {
		return vlUnitario;
	}

	public double getVlUnitarioEspecial() {
		return vlUnitarioEspecial;
	}

	public double getVlPctMaxDesconto() {
		return vlPctMaxDesconto;
	}

	public double getVlPctMaxAcrescimo() {
		return vlPctMaxAcrescimo;
	}

	public double getVlMaxDesconto() {
		return vlMaxDesconto;
	}

	public double getVlMaxAcrescimo() {
		return vlMaxAcrescimo;
	}

	public double getVlPrecoEmbalagemPrimaria() {
		return vlPrecoEmbalagemPrimaria;
	}

	public String getFlPromocao() {
		return flPromocao;
	}

	public double getVlBase() {
		return vlBase;
	}

	public double getVlBaseEspecial() {
		return vlBaseEspecial;
	}

	public double getVlPctComissao() {
		return vlPctComissao;
	}

	public double getVlPctDescPromocional() {
		return vlPctDescPromocional;
	}

	public double getVlPrecoFrete() {
		return vlPrecoFrete;
	}

	public double getVlReducaoOptanteSimples() {
		return vlReducaoOptanteSimples;
	}

	public String getFlBloqueado() {
		return flBloqueado;
	}

	public double getQtMaxVenda() {
		return qtMaxVenda;
	}

	public double getVlPrecoEmbalagem() {
		return vlPrecoEmbalagem;
	}

	public double getNuConversaoUnidade() {
		return nuConversaoUnidade;
	}

	public double getVlOportunidade() {
		return vlOportunidade;
	}

	public String getFlPrecoQueda() {
		return flPrecoQueda;
	}

	public String getFlUsaDesconto3() {
		return flUsaDesconto3;
	}

	public double getVlPctPrevisaRentabilidade() {
		return vlPctPrevisaRentabilidade;
	}

	public double getVlPctMargemRentabilidade() {
		return vlPctMargemRentabilidade;
	}

	public int getNuPromocao() {
		return nuPromocao;
	}

	public double getVlDescontoPromo() {
		return vlDescontoPromo;
	}

	public String getDtInicioPromocao() {
		return dtInicioPromocao == null ? null : DateUtil.formatDateDDMMYYYY(dtInicioPromocao);
	}

	public int getQtMinItensNormais() {
		return qtMinItensNormais;
	}

	public double getVlPctRentabilidadeEsp() {
		return vlPctRentabilidadeEsp;
	}

	public double getVlPctRentabilidadeMin() {
		return vlPctRentabilidadeMin;
	}

	public double getVlPctMaxParticipacao() {
		return vlPctMaxParticipacao;
	}

	public double getVlMinItemPedido() {
		return vlMinItemPedido;
	}
	
	public double getVlEmbalagemSt() {
		return vlEmbalagemSt;
	}

	public void setVlEmbalagemSt(double vlEmbalagemSt) {
		this.vlEmbalagemSt = vlEmbalagemSt;
	}
	
}
