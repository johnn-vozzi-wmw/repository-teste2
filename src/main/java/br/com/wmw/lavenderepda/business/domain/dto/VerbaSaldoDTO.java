package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import totalcross.util.Date;

public class VerbaSaldoDTO {
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String cdContaCorrente;
    public String flOrigemSaldo;
    public double vlSaldo;
    public double vlSaldoInicial;
    public Date dtSaldo;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public double vlMinVerba;
    public String flTipoAlteracao;
    
    public VerbaSaldoDTO(VerbaSaldo verbaSaldo) {
    	try {
    		FieldMapper.copy(verbaSaldo, this);
    		this.cdEmpresa = verbaSaldo.getCdEmpresa();
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
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
	
    public String getCdContaCorrente() {
		return cdContaCorrente;
	}
	
    public void setCdContaCorrente(String cdContaCorrente) {
		this.cdContaCorrente = cdContaCorrente;
	}
	
    public String getFlOrigemSaldo() {
		return flOrigemSaldo;
	}
	
    public void setFlOrigemSaldo(String flOrigemSaldo) {
		this.flOrigemSaldo = flOrigemSaldo;
	}
	
    public double getVlSaldo() {
		return vlSaldo;
	}
	
    public void setVlSaldo(double vlSaldo) {
		this.vlSaldo = vlSaldo;
	}
	
    public double getVlSaldoInicial() {
		return vlSaldoInicial;
	}
	
    public void setVlSaldoInicial(double vlSaldoInicial) {
		this.vlSaldoInicial = vlSaldoInicial;
	}
	
    public String getDtSaldo() {
		return dtSaldo == null ? null : DateUtil.formatDateDDMMYYYY(dtSaldo);
	}
	
    public void setDtSaldo(Date dtSaldo) {
		this.dtSaldo = dtSaldo;
	}
	
    public String getDtVigenciaInicial() {
		return dtVigenciaInicial == null ? null : DateUtil.formatDateDDMMYYYY(dtVigenciaInicial);
	}
	
    public void setDtVigenciaInicial(Date dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	
    public String getDtVigenciaFinal() {
		return dtVigenciaFinal == null ? null : DateUtil.formatDateDDMMYYYY(dtVigenciaFinal);
	}
	
    public void setDtVigenciaFinal(Date dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
    public double getVlMinVerba() {
		return vlMinVerba;
	}
	
    public void setVlMinVerba(double vlMinVerba) {
		this.vlMinVerba = vlMinVerba;
	}

	public String getFlTipoAlteracao() {
		return flTipoAlteracao;
	}

	public void setFlTipoAlteracao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

}
