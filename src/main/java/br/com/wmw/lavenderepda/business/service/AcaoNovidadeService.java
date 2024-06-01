package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.business.domain.AcaoNovidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AcaoNovidadeDao;

public class AcaoNovidadeService extends CrudService {

    private static AcaoNovidadeService instance;
    
    private AcaoNovidadeService() {
        //--
    }
    
    public static AcaoNovidadeService getInstance() {
        if (instance == null) {
            instance = new AcaoNovidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AcaoNovidadeDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public void insertAcaoNovidade(String acao, String cdRepresentante, String cdNovidade) throws SQLException {
    	AcaoNovidade acaoNovidade = new AcaoNovidade();
    	acaoNovidade.cdSistema = LavendereConfig.CDSISTEMALAVENDEREPDA;
    	acaoNovidade.cdNovidade = cdNovidade;
    	acaoNovidade.cdRepresentante = cdRepresentante;
    	acaoNovidade.cdAcaoNovidade = AcaoNovidadeDao.getInstance().getNextCdAcaoNovidade(acaoNovidade);
    	acaoNovidade.flAcao = acao;
    	acaoNovidade.dtAcao = DateUtil.getCurrentDate();
    	acaoNovidade.hrAcao = TimeUtil.getCurrentTimeHHMM();
    	insert(acaoNovidade);
    }
}