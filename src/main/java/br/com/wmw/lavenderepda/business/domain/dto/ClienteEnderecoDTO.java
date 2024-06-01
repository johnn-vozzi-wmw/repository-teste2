package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;

public class ClienteEnderecoDTO {

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String cdEndereco;
	public String dsLogradouro;
	public String nuLogradouro;
	public String dsComplemento;
	public String dsBairro;
	public String dsCep;
	public String dsCidade;
	public String dsEstado;
	public String dsPais;
	public String dsPontoReferencia;
	public String dsObservacao;
	public String flComercial;
	public String flEntrega;
	public String flEntregaPadrao;
	public String dsDiaEntrega;
	public String dsPeriodoEntrega;
	public String dsPeriodoEntregaAlternativo;
	public String dsDiaAbertura;
	public String dsPeriodoAbertura;
	public String flEntregaAgendada;
	public String cdPeriodoEntrega;
	public String cdTipoEndereco;
	public String flCobranca;
	public String flCobrancaPadrao;
	public String nuCnpj;
	public String flTipoPessoa;
	public String cdRegistro;

	public ClienteEnderecoDTO() {
		super();
	}

	public ClienteEnderecoDTO copy(final ClienteEndereco clienteEndereco) {
		try {
			FieldMapper.copy(clienteEndereco, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public String getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getCdEndereco() {
		return cdEndereco;
	}

	public void setCdEndereco(String cdEndereco) {
		this.cdEndereco = cdEndereco;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public String getNuLogradouro() {
		return nuLogradouro;
	}

	public void setNuLogradouro(String nuLogradouro) {
		this.nuLogradouro = nuLogradouro;
	}

	public String getDsComplemento() {
		return dsComplemento;
	}

	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}

	public String getDsBairro() {
		return dsBairro;
	}

	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}

	public String getDsCep() {
		return dsCep;
	}

	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
	}

	public String getDsCidade() {
		return dsCidade;
	}

	public void setDsCidade(String dsCidade) {
		this.dsCidade = dsCidade;
	}

	public String getDsEstado() {
		return dsEstado;
	}

	public void setDsEstado(String dsEstado) {
		this.dsEstado = dsEstado;
	}

	public String getDsPais() {
		return dsPais;
	}

	public void setDsPais(String dsPais) {
		this.dsPais = dsPais;
	}

	public String getDsPontoReferencia() {
		return dsPontoReferencia;
	}

	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getFlComercial() {
		return flComercial;
	}

	public void setFlComercial(String flComercial) {
		this.flComercial = flComercial;
	}

	public String getFlEntrega() {
		return flEntrega;
	}

	public void setFlEntrega(String flEntrega) {
		this.flEntrega = flEntrega;
	}

	public String getFlEntregaPadrao() {
		return flEntregaPadrao;
	}

	public void setFlEntregaPadrao(String flEntregaPadrao) {
		this.flEntregaPadrao = flEntregaPadrao;
	}

	public String getDsDiaEntrega() {
		return dsDiaEntrega;
	}

	public void setDsDiaEntrega(String dsDiaEntrega) {
		this.dsDiaEntrega = dsDiaEntrega;
	}

	public String getDsPeriodoEntrega() {
		return dsPeriodoEntrega;
	}

	public void setDsPeriodoEntrega(String dsPeriodoEntrega) {
		this.dsPeriodoEntrega = dsPeriodoEntrega;
	}

	public String getDsPeriodoEntregaAlternativo() {
		return dsPeriodoEntregaAlternativo;
	}

	public void setDsPeriodoEntregaAlternativo(String dsPeriodoEntregaAlternativo) {
		this.dsPeriodoEntregaAlternativo = dsPeriodoEntregaAlternativo;
	}

	public String getDsDiaAbertura() {
		return dsDiaAbertura;
	}

	public void setDsDiaAbertura(String dsDiaAbertura) {
		this.dsDiaAbertura = dsDiaAbertura;
	}

	public String getDsPeriodoAbertura() {
		return dsPeriodoAbertura;
	}

	public void setDsPeriodoAbertura(String dsPeriodoAbertura) {
		this.dsPeriodoAbertura = dsPeriodoAbertura;
	}

	public String getFlEntregaAgendada() {
		return flEntregaAgendada;
	}

	public void setFlEntregaAgendada(String flEntregaAgendada) {
		this.flEntregaAgendada = flEntregaAgendada;
	}

	public String getCdPeriodoEntrega() {
		return cdPeriodoEntrega;
	}

	public void setCdPeriodoEntrega(String cdPeriodoEntrega) {
		this.cdPeriodoEntrega = cdPeriodoEntrega;
	}

	public String getCdTipoEndereco() {
		return cdTipoEndereco;
	}

	public void setCdTipoEndereco(String cdTipoEndereco) {
		this.cdTipoEndereco = cdTipoEndereco;
	}

	public String getFlCobranca() {
		return flCobranca;
	}

	public void setFlCobranca(String flCobranca) {
		this.flCobranca = flCobranca;
	}

	public String getFlCobrancaPadrao() {
		return flCobrancaPadrao;
	}

	public void setFlCobrancaPadrao(String flCobrancaPadrao) {
		this.flCobrancaPadrao = flCobrancaPadrao;
	}

	public String getNuCnpj() {
		return nuCnpj;
	}

	public void setNuCnpj(String nuCnpj) {
		this.nuCnpj = nuCnpj;
	}

	public String getFlTipoPessoa() {
		return flTipoPessoa;
	}

	public void setFlTipoPessoa(String flTipoPessoa) {
		this.flTipoPessoa = flTipoPessoa;
	}

	public String getCdRegistro() {
		return cdRegistro;
	}

	public void setCdRegistro(String cdRegistro) {
		this.cdRegistro = cdRegistro;
	}

}