package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;

public class TabelaPrecoDTO {

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String dsTabelaPreco;
	
    public TabelaPrecoDTO() {
		super();
	}
    
    public TabelaPrecoDTO copy(final TabelaPreco tabelaPreco) {
  		try {
  			FieldMapper.copy(tabelaPreco, this);
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

	public String getDsTabelaPreco() {
		return dsTabelaPreco;
	}
    
}
