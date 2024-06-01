package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;

public class DapLaudoDTO {
	
	public String cdEmpresa;
	public String cdCliente;
	public String cdDapMatricula;
	public String cdSafra;
	public String cdDapCultura;
	public String cdDapLaudo;

	public DapLaudoDTO(final DapLaudo dapLaudo) {
		super();
		try {
			FieldMapper.copy(dapLaudo, this);
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

	public String getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getCdDapMatricula() {
		return cdDapMatricula;
	}

	public void setCdDapMatricula(String cdDapMatricula) {
		this.cdDapMatricula = cdDapMatricula;
	}

	public String getCdSafra() {
		return cdSafra;
	}

	public void setCdSafra(String cdSafra) {
		this.cdSafra = cdSafra;
	}

	public String getCdDapCultura() {
		return cdDapCultura;
	}

	public void setCdDapCultura(String cdDapCultura) {
		this.cdDapCultura = cdDapCultura;
	}

	public String getCdDapLaudo() {
		return cdDapLaudo;
	}

	public void setCdDapLaudo(String cdDapLaudo) {
		this.cdDapLaudo = cdDapLaudo;
	}

}
