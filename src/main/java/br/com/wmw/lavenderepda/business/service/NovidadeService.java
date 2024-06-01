package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Novidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovidadeDbxDao;
import totalcross.util.Vector;

public class NovidadeService extends CrudService {

    private static NovidadeService instance;
    
    private NovidadeService() {
        //--
    }
    
    public static NovidadeService getInstance() {
        if (instance == null) {
            instance = new NovidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NovidadeDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
	public Novidade findNovidadePendenteLeitura(int cdSistema) throws SQLException {
		Novidade novidadeFilter = new Novidade();
    	novidadeFilter.cdSistema = cdSistema;
		Vector novidadeList = NovidadeService.getInstance().findAllByExample(novidadeFilter);
		if (ValueUtil.isNotEmpty(novidadeList)) {
			Novidade novidade = (Novidade)novidadeList.items[0];
			if (novidade.isMostraNovidade(cdSistema, novidade.cdNovidade)) {
				return novidade;
			}
		}
		return novidadeFilter;
	}
}