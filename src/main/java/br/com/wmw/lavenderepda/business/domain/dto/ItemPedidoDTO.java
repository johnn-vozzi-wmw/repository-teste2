package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import totalcross.util.Date;

public class ItemPedidoDTO {

	public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public int nuSeqItemPedido;
    public String cdTabelaPreco;
    public double qtItemFisico;
    public double qtItemFaturamento;
    public double qtPeso;
    public double vlItemPedido;
    public double vlBaseItemTabelaPreco;
    public double vlBaseCalculoDescPromocional;
    public double vlBaseItemPedido;
    public double vlBaseFlex;
    public double vlTotalItemPedido;
    public double vlPctDesconto;
    public double vlPctAcrescimo;
    public double vlRentabilidade;
    public double vlPctComissao;
    public int cdLinha;
    public double vlVerbaItem;
    public double vlVerbaItemPositivo;
    public double vlItemPedidoFrete;
    public double vlTotalItemPedidoFrete;
    public String cdContaCorrente;
    public String cdLoteProduto;
    public String flPrecoLiberadoSenha;
    public double vlSt;
    public double vlReducaoOptanteSimples;
    public String cdMotivoTroca;
    public String dsObsMotivoTroca;
    public String dsObservacao;
    public int qtPontosItem;
    public String cdUnidade;
    public int cdPrazoPagtoPreco;
    public String flVendidoQtMinima;
    public double vlTotalBrutoItemPedido;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public double vlUnidadePadrao;
    public double vlItemIpi;
    public double vlBaseItemIpi;
    public double vlBaseEmbalagemElementar;
    public double vlItemPedidoUnElementar;
    public double qtItemPedidoUnElementar;
    public double vlFrete;
    public double vlFreteOld;
    public String flMetaGrupoProdLiberadoSenha;
    public String flLiberadoVendaRelacionada;
    public double vlIpiItem;
    public double qtItemEstoquePositivo;
    public double vlDescontoCCP;
    public double vlPctDescontoCCP;
    public double vlFinalPromo;
    public double vlPctDescontoPromo;
    public double vlPctFaixaDescQtd;
    public double vlFecop;
    public double vlTotalFecopItem;
    public double vlIcms;
    public double vlPis;
    public double vlCofins;
    public double vlDespesaAcessoria;
    public String cdSugestaoVenda;
    public String cdTributacaoConfig;
    public double vlPctDescontoCanal;
    public double vlSeguroItemPedido;
    public double vlDescontoPromo;
    public Date dtInicioPromocao;
    public int nuPromocao;
    public String flEstoqueLiberado;
    public String cdCondicaoComercial;
    public double vlCreditoFrete;
    public double vlCreditoCondicao;
    public double vlDesconto;
    public double vlPctDesconto2;
    public double vlDesconto2;
    public double vlPctDesconto3;
    public double vlDesconto3;
    public int cdVerbaGrupo;
    public double vlTotalStItem;
    public double vlTotalIpiItem;
    public double vlTotalIcmsItem;
    public double vlTotalPisItem;
    public double vlTotalCofinsItem;
    public String cdUsuarioLiberacao;
    public String flQuantidadeLiberada;
    public String flPendente;
    public double vlPctDescProgressivoMix;
    public double vlVolumeItem;
	public double vlPctDescFrete;
	public double vlPctDescCliente;
	public double vlPctDescontoCondicao;
	public double vlPctVerbaRateio;
	public double vlPctMargemProduto;
	public String cdVerba;
	public double vlPctContratoCli;
	public String flDecisaoCalculo;
	public double vlItemPedidoStReverso;
    public double vlPctDescontoStReverso;
    public String flRestrito;
    public String flPromocao;
    public String flValorTabelaAlterado;
    public ItemPedidoGradeDTO[] itemPedidoGradeErpList;
    public double vlPctDescCondPagto;
    public String cdProdutoCliente;
	public double qtEncomenda;
	public double vlTotalEncomenda;
	public String flAgrupadorSimilaridade;
	public String infosPersonalizadas;
	public double nuConversaoUnidadePu;
	public String flDivideMultiplicaPu;
	public String cdKit;
    
	public ItemPedidoDTO() {
		super();
	}
	
	public ItemPedidoGradeDTO[] getItemPedidoGradeErpList() {
		return itemPedidoGradeErpList;
	}

	public void setItemPedidoGradeErpList(ItemPedidoGradeDTO[] itemPedidoGradeErpList) {
		this.itemPedidoGradeErpList = itemPedidoGradeErpList;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	public String getNuPedido() {
		return nuPedido;
	}
	public String getCdProduto() {
		return cdProduto;
	}
	public String getFlTipoItemPedido() {
		return flTipoItemPedido;
	}
	public int getNuSeqProduto() {
		return nuSeqProduto;
	}
	public int getNuSeqItemPedido() {
		return nuSeqItemPedido;
	}
	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}
	public double getQtItemFisico() {
		return ValueUtil.round(qtItemFisico);
	}
	public double getQtItemFaturamento() {
		return ValueUtil.round(qtItemFaturamento);
	}
	public double getQtPeso() {
		return ValueUtil.round(qtPeso);
	}
	public double getVlItemPedido() {
		return ValueUtil.round(vlItemPedido);
	}
	public double getVlBaseItemTabelaPreco() {
		return ValueUtil.round(vlBaseItemTabelaPreco);
	}
	public double getVlBaseCalculoDescPromocional() {
		return ValueUtil.round(vlBaseCalculoDescPromocional);
	}
	public double getVlBaseItemPedido() {
		return ValueUtil.round(vlBaseItemPedido);
	}
	public double getVlBaseFlex() {
		return ValueUtil.round(vlBaseFlex);
	}
	public double getVlTotalItemPedido() {
		return ValueUtil.round(vlTotalItemPedido);
	}
	public double getVlPctDesconto() {
		return ValueUtil.round(vlPctDesconto);
	}
	public double getVlPctAcrescimo() {
		return ValueUtil.round(vlPctAcrescimo);
	}
	public double getVlRentabilidade() {
		return ValueUtil.round(vlRentabilidade);
	}
	public double getVlPctComissao() {
		return ValueUtil.round(vlPctComissao);
	}
	public int getCdLinha() {
		return cdLinha;
	}
	public double getVlVerbaItem() {
		return ValueUtil.round(vlVerbaItem);
	}
	public double getVlVerbaItemPositivo() {
		return ValueUtil.round(vlVerbaItemPositivo);
	}
	public double getVlItemPedidoFrete() {
		return ValueUtil.round(vlItemPedidoFrete);
	}
	public double getVlTotalItemPedidoFrete() {
		return ValueUtil.round(vlTotalItemPedidoFrete);
	}
	public String getCdContaCorrente() {
		return cdContaCorrente;
	}
	public String getCdLoteProduto() {
		return cdLoteProduto;
	}
	public String getFlPrecoLiberadoSenha() {
		return flPrecoLiberadoSenha;
	}
	public double getVlSt() {
		return ValueUtil.round(vlSt);
	}
	public double getVlReducaoOptanteSimples() {
		return ValueUtil.round(vlReducaoOptanteSimples);
	}
	public String getCdMotivoTroca() {
		return cdMotivoTroca;
	}
	public String getDsObsMotivoTroca() {
		return dsObsMotivoTroca;
	}
	public String getDsObservacao() {
		return dsObservacao;
	}
	public int getQtPontosItem() {
		return qtPontosItem;
	}
	public String getCdUnidade() {
		return cdUnidade;
	}
	public int getCdPrazoPagtoPreco() {
		return cdPrazoPagtoPreco;
	}
	public String getFlVendidoQtMinima() {
		return flVendidoQtMinima;
	}
	public double getVlTotalBrutoItemPedido() {
		return ValueUtil.round(vlTotalBrutoItemPedido);
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
	public double getVlUnidadePadrao() {
		return ValueUtil.round(vlUnidadePadrao);
	}
	public double getVlItemIpi() {
		return ValueUtil.round(vlItemIpi);
	}
	public double getVlBaseItemIpi() {
		return ValueUtil.round(vlBaseItemIpi);
	}
	public double getVlBaseEmbalagemElementar() {
		return ValueUtil.round(vlBaseEmbalagemElementar);
	}
	public double getVlItemPedidoUnElementar() {
		return ValueUtil.round(vlItemPedidoUnElementar);
	}
	public double getQtItemPedidoUnElementar() {
		return ValueUtil.round(qtItemPedidoUnElementar);
	}
	public double getVlFrete() {
		return ValueUtil.round(vlFrete);
	}
	public double getVlFreteOld() {
		return ValueUtil.round(vlFreteOld);
	}
	public String getFlMetaGrupoProdLiberadoSenha() {
		return flMetaGrupoProdLiberadoSenha;
	}
	public String getFlLiberadoVendaRelacionada() {
		return flLiberadoVendaRelacionada;
	}
	public double getVlIpiItem() {
		return ValueUtil.round(vlIpiItem);
	}
	public double getQtItemEstoquePositivo() {
		return ValueUtil.round(qtItemEstoquePositivo);
	}
	public double getVlDescontoCCP() {
		return ValueUtil.round(vlDescontoCCP);
	}
	public double getVlPctDescontoCCP() {
		return ValueUtil.round(vlPctDescontoCCP);
	}
	public double getVlFinalPromo() {
		return ValueUtil.round(vlFinalPromo);
	}
	public double getVlPctDescontoPromo() {
		return ValueUtil.round(vlPctDescontoPromo);
	}
	public double getVlPctFaixaDescQtd() {
		return ValueUtil.round(vlPctFaixaDescQtd);
	}
	public double getVlFecop() {
		return ValueUtil.round(vlFecop);
	}
	public double getVlIcms() {
		return ValueUtil.round(vlIcms);
	}
	public double getVlPis() {
		return ValueUtil.round(vlPis);
	}
	public double getVlCofins() {
		return ValueUtil.round(vlCofins);
	}
	public double getVlDespesaAcessoria() {
		return ValueUtil.round(vlDespesaAcessoria);
	}
	public String getCdSugestaoVenda() {
		return cdSugestaoVenda;
	}
	public String getCdTributacaoConfig() {
		return cdTributacaoConfig;
	}
	public double getVlPctDescontoCanal() {
		return ValueUtil.round(vlPctDescontoCanal);
	}
	public double getVlSeguroItemPedido() {
		return ValueUtil.round(vlSeguroItemPedido);
	}
	public double getVlDescontoPromo() {
		return ValueUtil.round(vlDescontoPromo);
	}
	public Date getDtInicioPromocao() {
		return dtInicioPromocao;
	}
	public int getNuPromocao() {
		return nuPromocao;
	}
	public String getFlEstoqueLiberado() {
		return flEstoqueLiberado;
	}
	public String getCdCondicaoComercial() {
		return cdCondicaoComercial;
	}
	public double getVlCreditoFrete() {
		return ValueUtil.round(vlCreditoFrete);
	}
	public double getVlCreditoCondicao() {
		return ValueUtil.round(vlCreditoCondicao);
	}
	public double getVlDesconto() {
		return ValueUtil.round(vlDesconto);
	}
	public double getVlPctDesconto2() {
		return ValueUtil.round(vlPctDesconto2);
	}
	public double getVlDesconto2() {
		return ValueUtil.round(vlDesconto2);
	}
	public double getVlPctDesconto3() {
		return ValueUtil.round(vlPctDesconto3);
	}
	public double getVlDesconto3() {
		return ValueUtil.round(vlDesconto3);
	}
	public int getCdVerbaGrupo() {
		return cdVerbaGrupo;
	}
	public double getVlTotalStItem() {
		return ValueUtil.round(vlTotalStItem);
	}
	public double getVlTotalIpiItem() {
		return ValueUtil.round(vlTotalIpiItem);
	}
	public double getVlTotalIcmsItem() {
		return ValueUtil.round(vlTotalIcmsItem);
	}
	public double getVlTotalPisItem() {
		return ValueUtil.round(vlTotalPisItem);
	}
	public double getVlTotalCofinsItem() {
		return ValueUtil.round(vlTotalCofinsItem);
	}
	public String getCdUsuarioLiberacao() {
		return cdUsuarioLiberacao;
	}
	public String getFlQuantidadeLiberada() {
		return flQuantidadeLiberada;
	}
	public String getFlPendente() {
		return flPendente;
	}
	public double getVlPctDescProgressivoMix() {
		return ValueUtil.round(vlPctDescProgressivoMix);
	}
	public double getVlVolumeItem() {
		return ValueUtil.round(vlVolumeItem);
	}
	public double getVlPctDescFrete() {
		return ValueUtil.round(vlPctDescFrete);
	}
	public double getVlPctDescCliente() {
		return ValueUtil.round(vlPctDescCliente);
	}
	public double getVlPctDescontoCondicao() {
		return ValueUtil.round(vlPctDescontoCondicao);
	}
	public double getVlPctVerbaRateio() {
		return ValueUtil.round(vlPctVerbaRateio);
	}
	public double getVlPctMargemProduto() {
		return ValueUtil.round(vlPctMargemProduto);
	}
	public String getCdVerba() {
		return cdVerba;
	}
	public double getVlPctContratoCli() {
		return ValueUtil.round(vlPctContratoCli);
	}
	public String getFlDecisaoCalculo() {
		return flDecisaoCalculo;
	}
	public double getVlItemPedidoStReverso() {
		return ValueUtil.round(vlItemPedidoStReverso);
	}
	public double getVlPctDescontoStReverso() {
		return ValueUtil.round(vlPctDescontoStReverso);
	}
	public String getFlRestrito() {
		return flRestrito;
	}
	public String getFlPromocao() {
		return flPromocao;
	}
	public String getFlValorTabelaAlterado() {
		return flValorTabelaAlterado;
	}
	
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}

	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}

	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}

	public void setFlTipoItemPedido(String flTipoItemPedido) {
		this.flTipoItemPedido = flTipoItemPedido;
	}

	public void setNuSeqProduto(int nuSeqProduto) {
		this.nuSeqProduto = nuSeqProduto;
	}

	public void setNuSeqItemPedido(int nuSeqItemPedido) {
		this.nuSeqItemPedido = nuSeqItemPedido;
	}

	public void setCdTabelaPreco(String cdTabelaPreco) {
		this.cdTabelaPreco = cdTabelaPreco;
	}

	public void setQtItemFisico(double qtItemFisico) {
		this.qtItemFisico = qtItemFisico;
	}

	public void setQtItemFaturamento(double qtItemFaturamento) {
		this.qtItemFaturamento = qtItemFaturamento;
	}

	public void setQtPeso(double qtPeso) {
		this.qtPeso = qtPeso;
	}

	public void setVlItemPedido(double vlItemPedido) {
		this.vlItemPedido = vlItemPedido;
	}

	public void setVlBaseItemTabelaPreco(double vlBaseItemTabelaPreco) {
		this.vlBaseItemTabelaPreco = vlBaseItemTabelaPreco;
	}

	public void setVlBaseCalculoDescPromocional(double vlBaseCalculoDescPromocional) {
		this.vlBaseCalculoDescPromocional = vlBaseCalculoDescPromocional;
	}

	public void setVlBaseItemPedido(double vlBaseItemPedido) {
		this.vlBaseItemPedido = vlBaseItemPedido;
	}

	public void setVlBaseFlex(double vlBaseFlex) {
		this.vlBaseFlex = vlBaseFlex;
	}

	public void setVlTotalItemPedido(double vlTotalItemPedido) {
		this.vlTotalItemPedido = vlTotalItemPedido;
	}

	public void setVlPctDesconto(double vlPctDesconto) {
		this.vlPctDesconto = vlPctDesconto;
	}

	public void setVlPctAcrescimo(double vlPctAcrescimo) {
		this.vlPctAcrescimo = vlPctAcrescimo;
	}

	public void setVlRentabilidade(double vlRentabilidade) {
		this.vlRentabilidade = vlRentabilidade;
	}

	public void setVlPctComissao(double vlPctComissao) {
		this.vlPctComissao = vlPctComissao;
	}

	public void setCdLinha(int cdLinha) {
		this.cdLinha = cdLinha;
	}

	public void setVlVerbaItem(double vlVerbaItem) {
		this.vlVerbaItem = vlVerbaItem;
	}

	public void setVlVerbaItemPositivo(double vlVerbaItemPositivo) {
		this.vlVerbaItemPositivo = vlVerbaItemPositivo;
	}

	public void setVlItemPedidoFrete(double vlItemPedidoFrete) {
		this.vlItemPedidoFrete = vlItemPedidoFrete;
	}

	public void setVlTotalItemPedidoFrete(double vlTotalItemPedidoFrete) {
		this.vlTotalItemPedidoFrete = vlTotalItemPedidoFrete;
	}

	public void setCdContaCorrente(String cdContaCorrente) {
		this.cdContaCorrente = cdContaCorrente;
	}

	public void setCdLoteProduto(String cdLoteProduto) {
		this.cdLoteProduto = cdLoteProduto;
	}

	public void setFlPrecoLiberadoSenha(String flPrecoLiberadoSenha) {
		this.flPrecoLiberadoSenha = flPrecoLiberadoSenha;
	}

	public void setVlSt(double vlSt) {
		this.vlSt = vlSt;
	}

	public void setVlReducaoOptanteSimples(double vlReducaoOptanteSimples) {
		this.vlReducaoOptanteSimples = vlReducaoOptanteSimples;
	}

	public void setCdMotivoTroca(String cdMotivoTroca) {
		this.cdMotivoTroca = cdMotivoTroca;
	}

	public void setDsObsMotivoTroca(String dsObsMotivoTroca) {
		this.dsObsMotivoTroca = dsObsMotivoTroca;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public void setQtPontosItem(int qtPontosItem) {
		this.qtPontosItem = qtPontosItem;
	}

	public void setCdUnidade(String cdUnidade) {
		this.cdUnidade = cdUnidade;
	}

	public void setCdPrazoPagtoPreco(int cdPrazoPagtoPreco) {
		this.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
	}

	public void setFlVendidoQtMinima(String flVendidoQtMinima) {
		this.flVendidoQtMinima = flVendidoQtMinima;
	}

	public void setVlTotalBrutoItemPedido(double vlTotalBrutoItemPedido) {
		this.vlTotalBrutoItemPedido = vlTotalBrutoItemPedido;
	}

	public void setCdItemGrade1(String cdItemGrade1) {
		this.cdItemGrade1 = cdItemGrade1;
	}

	public void setCdItemGrade2(String cdItemGrade2) {
		this.cdItemGrade2 = cdItemGrade2;
	}

	public void setCdItemGrade3(String cdItemGrade3) {
		this.cdItemGrade3 = cdItemGrade3;
	}

	public void setVlUnidadePadrao(double vlUnidadePadrao) {
		this.vlUnidadePadrao = vlUnidadePadrao;
	}

	public void setVlItemIpi(double vlItemIpi) {
		this.vlItemIpi = vlItemIpi;
	}

	public void setVlBaseItemIpi(double vlBaseItemIpi) {
		this.vlBaseItemIpi = vlBaseItemIpi;
	}

	public void setVlBaseEmbalagemElementar(double vlBaseEmbalagemElementar) {
		this.vlBaseEmbalagemElementar = vlBaseEmbalagemElementar;
	}

	public void setVlItemPedidoUnElementar(double vlItemPedidoUnElementar) {
		this.vlItemPedidoUnElementar = vlItemPedidoUnElementar;
	}

	public void setQtItemPedidoUnElementar(double qtItemPedidoUnElementar) {
		this.qtItemPedidoUnElementar = qtItemPedidoUnElementar;
	}

	public void setVlFrete(double vlFrete) {
		this.vlFrete = vlFrete;
	}

	public void setVlFreteOld(double vlFreteOld) {
		this.vlFreteOld = vlFreteOld;
	}

	public void setFlMetaGrupoProdLiberadoSenha(String flMetaGrupoProdLiberadoSenha) {
		this.flMetaGrupoProdLiberadoSenha = flMetaGrupoProdLiberadoSenha;
	}

	public void setFlLiberadoVendaRelacionada(String flLiberadoVendaRelacionada) {
		this.flLiberadoVendaRelacionada = flLiberadoVendaRelacionada;
	}

	public void setVlIpiItem(double vlIpiItem) {
		this.vlIpiItem = vlIpiItem;
	}

	public void setQtItemEstoquePositivo(double qtItemEstoquePositivo) {
		this.qtItemEstoquePositivo = qtItemEstoquePositivo;
	}

	public void setVlDescontoCCP(double vlDescontoCCP) {
		this.vlDescontoCCP = vlDescontoCCP;
	}

	public void setVlPctDescontoCCP(double vlPctDescontoCCP) {
		this.vlPctDescontoCCP = vlPctDescontoCCP;
	}

	public void setVlFinalPromo(double vlFinalPromo) {
		this.vlFinalPromo = vlFinalPromo;
	}

	public void setVlPctDescontoPromo(double vlPctDescontoPromo) {
		this.vlPctDescontoPromo = vlPctDescontoPromo;
	}

	public void setVlPctFaixaDescQtd(double vlPctFaixaDescQtd) {
		this.vlPctFaixaDescQtd = vlPctFaixaDescQtd;
	}

	public void setVlFecop(double vlFecop) {
		this.vlFecop = vlFecop;
	}

	public void setVlIcms(double vlIcms) {
		this.vlIcms = vlIcms;
	}

	public void setVlPis(double vlPis) {
		this.vlPis = vlPis;
	}

	public void setVlCofins(double vlCofins) {
		this.vlCofins = vlCofins;
	}

	public void setVlDespesaAcessoria(double vlDespesaAcessoria) {
		this.vlDespesaAcessoria = vlDespesaAcessoria;
	}

	public void setCdSugestaoVenda(String cdSugestaoVenda) {
		this.cdSugestaoVenda = cdSugestaoVenda;
	}

	public void setCdTributacaoConfig(String cdTributacaoConfig) {
		this.cdTributacaoConfig = cdTributacaoConfig;
	}

	public void setVlPctDescontoCanal(double vlPctDescontoCanal) {
		this.vlPctDescontoCanal = vlPctDescontoCanal;
	}

	public void setVlSeguroItemPedido(double vlSeguroItemPedido) {
		this.vlSeguroItemPedido = vlSeguroItemPedido;
	}

	public void setVlDescontoPromo(double vlDescontoPromo) {
		this.vlDescontoPromo = vlDescontoPromo;
	}

	public void setDtInicioPromocao(Date dtInicioPromocao) {
		this.dtInicioPromocao = dtInicioPromocao;
	}

	public void setNuPromocao(int nuPromocao) {
		this.nuPromocao = nuPromocao;
	}

	public void setFlEstoqueLiberado(String flEstoqueLiberado) {
		this.flEstoqueLiberado = flEstoqueLiberado;
	}

	public void setCdCondicaoComercial(String cdCondicaoComercial) {
		this.cdCondicaoComercial = cdCondicaoComercial;
	}

	public void setVlCreditoFrete(double vlCreditoFrete) {
		this.vlCreditoFrete = vlCreditoFrete;
	}

	public void setVlCreditoCondicao(double vlCreditoCondicao) {
		this.vlCreditoCondicao = vlCreditoCondicao;
	}

	public void setVlDesconto(double vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public void setVlPctDesconto2(double vlPctDesconto2) {
		this.vlPctDesconto2 = vlPctDesconto2;
	}

	public void setVlDesconto2(double vlDesconto2) {
		this.vlDesconto2 = vlDesconto2;
	}

	public void setVlPctDesconto3(double vlPctDesconto3) {
		this.vlPctDesconto3 = vlPctDesconto3;
	}

	public void setVlDesconto3(double vlDesconto3) {
		this.vlDesconto3 = vlDesconto3;
	}

	public void setCdVerbaGrupo(int cdVerbaGrupo) {
		this.cdVerbaGrupo = cdVerbaGrupo;
	}

	public void setVlTotalStItem(double vlTotalStItem) {
		this.vlTotalStItem = vlTotalStItem;
	}

	public void setVlTotalIpiItem(double vlTotalIpiItem) {
		this.vlTotalIpiItem = vlTotalIpiItem;
	}

	public void setVlTotalIcmsItem(double vlTotalIcmsItem) {
		this.vlTotalIcmsItem = vlTotalIcmsItem;
	}

	public void setVlTotalPisItem(double vlTotalPisItem) {
		this.vlTotalPisItem = vlTotalPisItem;
	}

	public void setVlTotalCofinsItem(double vlTotalCofinsItem) {
		this.vlTotalCofinsItem = vlTotalCofinsItem;
	}

	public void setCdUsuarioLiberacao(String cdUsuarioLiberacao) {
		this.cdUsuarioLiberacao = cdUsuarioLiberacao;
	}

	public void setFlQuantidadeLiberada(String flQuantidadeLiberada) {
		this.flQuantidadeLiberada = flQuantidadeLiberada;
	}

	public void setFlPendente(String flPendente) {
		this.flPendente = flPendente;
	}

	public void setVlPctDescProgressivoMix(double vlPctDescProgressivoMix) {
		this.vlPctDescProgressivoMix = vlPctDescProgressivoMix;
	}

	public void setVlVolumeItem(double vlVolumeItem) {
		this.vlVolumeItem = vlVolumeItem;
	}

	public void setVlPctDescFrete(double vlPctDescFrete) {
		this.vlPctDescFrete = vlPctDescFrete;
	}

	public void setVlPctDescCliente(double vlPctDescCliente) {
		this.vlPctDescCliente = vlPctDescCliente;
	}

	public void setVlPctDescontoCondicao(double vlPctDescontoCondicao) {
		this.vlPctDescontoCondicao = vlPctDescontoCondicao;
	}

	public void setVlPctVerbaRateio(double vlPctVerbaRateio) {
		this.vlPctVerbaRateio = vlPctVerbaRateio;
	}

	public void setVlPctMargemProduto(double vlPctMargemProduto) {
		this.vlPctMargemProduto = vlPctMargemProduto;
	}

	public void setCdVerba(String cdVerba) {
		this.cdVerba = cdVerba;
	}

	public void setVlPctContratoCli(double vlPctContratoCli) {
		this.vlPctContratoCli = vlPctContratoCli;
	}

	public void setFlDecisaoCalculo(String flDecisaoCalculo) {
		this.flDecisaoCalculo = flDecisaoCalculo;
	}

	public void setVlItemPedidoStReverso(double vlItemPedidoStReverso) {
		this.vlItemPedidoStReverso = vlItemPedidoStReverso;
	}

	public void setVlPctDescontoStReverso(double vlPctDescontoStReverso) {
		this.vlPctDescontoStReverso = vlPctDescontoStReverso;
	}

	public void setFlRestrito(String flRestrito) {
		this.flRestrito = flRestrito;
	}

	public void setFlPromocao(String flPromocao) {
		this.flPromocao = flPromocao;
	}

	public void setFlValorTabelaAlterado(String flValorTabelaAlterado) {
		this.flValorTabelaAlterado = flValorTabelaAlterado;
	}

	public double getVlPctDescCondPagto() {
		return vlPctDescCondPagto;
	}

	public void setVlPctDescCondPagto(double vlPctDescCondPagto) {
		this.vlPctDescCondPagto = vlPctDescCondPagto;
	}

	public ItemPedidoDTO copy(ItemPedido itemPedido) {
		try {
			FieldMapper.copy(itemPedido, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public double getVlTotalFecopItem() {
		return ValueUtil.round(this.vlTotalFecopItem);
	}

	public void setVlTotalFecopItem(double vlTotalFecopItem) {
		this.vlTotalFecopItem = vlTotalFecopItem;
	}

	public String getCdProdutoCliente() {
		return cdProdutoCliente;
	}

	public void setCdProdutoCliente(String cdProdutoCliente) {
		this.cdProdutoCliente = cdProdutoCliente;
	}

	public double getQtEncomenda() {
		return qtEncomenda;
	}

	public void setQtEncomenda(double qtEncomenda) {
		this.qtEncomenda = qtEncomenda;
	}

	public double getVlTotalEncomenda() {
		return vlTotalEncomenda;
	}

	public void setVlTotalEncomenda(double vlTotalEncomenda) {
		this.vlTotalEncomenda = vlTotalEncomenda;
	}

	public String getFlAgrupadorSimilaridade() {
		return flAgrupadorSimilaridade;
	}

	public void setFlAgrupadorSimilaridade(String flAgrupadorSimilaridade) {
		this.flAgrupadorSimilaridade = flAgrupadorSimilaridade;
	}

	public String getInfosPersonalizadas() {
		return infosPersonalizadas;
	}

	public void setInfosPersonalizadas(String infosPersonalizadas) {
		this.infosPersonalizadas = infosPersonalizadas;
	}
	
	public double getNuConversaoUnidadePu() {
		return nuConversaoUnidadePu;
	}
	
	public void setNuConversaoUnidadePu(double nuConversaoUnidadePu) {
		this.nuConversaoUnidadePu = nuConversaoUnidadePu;
	}
	
	public String getFlDivideMultiplicaPu() {
		return flDivideMultiplicaPu;
	}
	
	public void setNuConversaoUnidadePu(String flDivideMultiplicaPu) {
		this.flDivideMultiplicaPu = flDivideMultiplicaPu;
	}

	public String getCdKit() {
		return cdKit;
	}

	public void setCdKit(String cdKit) {
		this.cdKit = cdKit;
	}
	
}
