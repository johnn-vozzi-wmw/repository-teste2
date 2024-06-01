package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.FormulaCalculoSql;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FormulaCalculoSqlDbxDao;
import totalcross.util.Vector;
import totalcross.util.regex.Matcher;
import totalcross.util.regex.Pattern;

public class FormulaCalculoSqlService extends CrudService {

    private static FormulaCalculoSqlService instance;
    
    private FormulaCalculoSqlService() { }
    
    public static FormulaCalculoSqlService getInstance() {
        if (instance == null) {
            instance = new FormulaCalculoSqlService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FormulaCalculoSqlDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
    public HashMap<String, Object> executeCalculoSql(HashMap<String, Object> variaveis, String cdFormulaCalculo, BaseDomain domain) throws SQLException {
    	HashMap<String, Object> variaveisToSave = new HashMap<>();

    	if (variaveis == null || cdFormulaCalculo == null) return variaveisToSave;
    	
    	executeFormulasSql(findAllByExample(new FormulaCalculoSql(cdFormulaCalculo, FormulaCalculoSql.FLTIPOSISTEMA_PDA)), variaveisToSave, variaveis);
    	
    	populateDomainWithHashValues(domain, variaveisToSave);
    	
    	return variaveisToSave;
    }

	private void executeFormulasSql(Vector formulasCalculoSql, HashMap<String, Object> variaveisToSave, HashMap<String, Object> variaveis) throws SQLException {
		if (ValueUtil.isEmpty(formulasCalculoSql)) return;
		
		for (int i = 0; i < formulasCalculoSql.size(); i++) {
			FormulaCalculoSql formulaCalculoSql = (FormulaCalculoSql) formulasCalculoSql.items[i];
			
			String sql = replaceVariaveis(variaveis, formulaCalculoSql.dsSql).trim();
			
			Map<String, Object> hashResult = executaFormulaSql(sql);
			
			variaveisToSave.putAll(hashResult);
			variaveis.putAll(hashResult);
		}
	}

	private String replaceVariaveis(HashMap<String, Object> variaveis, String sql) throws SQLException {
		ArrayList<String> variablesToReplace = findVariablesToReplaceOnSql(sql);
		
		for (String variable : variablesToReplace) {
			sql = sql.replace(":"+variable+":", variaveis.get(variable) != null ? variaveis.get(variable).toString() : "null");
		}
		
		return sql;
	}

	private ArrayList<String> findVariablesToReplaceOnSql(String sql) {
		ArrayList<String> variablesToReplace = new ArrayList<>();
		int index = sql.indexOf(":");
		String result = sql.toString();
		
		while (index != -1) {
			result = result.substring(index+1);
			index = result.indexOf(":");
			if (index == -1) break;
			variablesToReplace.add(result.substring(0, index));
			result = result.substring(index+1);
			index = result.indexOf(":");
		}
		
		return variablesToReplace;
	}

	private Map<String, Object> executaFormulaSql(String sql) throws SQLException {
		return FormulaCalculoSqlDbxDao.getInstance().executaFormulaSql(resolveFunctions(sql));
	}
	
	public String resolveFunctions(String sql) throws SQLException {
		Pattern p = Pattern.compile("(TEXT)\\((.+?)\\)|((POWER)\\((.*?,(\\s|\\s\\-|\\s\\+)\\({1}.*?\\))\\){1})(\\s|\\/|\\*|\\+|\\-)|((POWER)\\((.*?,(\\s|\\s\\-)\\({0}.*?)\\){1})(\\s|\\/|\\*|\\+|\\-)");
		Matcher m = p.matcher(sql);
		while (m.find()) {
			String typeFunction = m.groups()[1] == null ? m.groups()[4] == null ?  m.groups()[9] : m.groups()[4] : m.groups()[1];
			switch (typeFunction) {
			case "TEXT":
				sql = resolveText(sql, m.groups());
				break;
			case "POWER":
				sql = resolvePower(sql, m.groups());
				break;
			default:
				break;
			}
		}
		return sql;
	}
	
	public String resolvePower(String sql, String[] resultPower) throws SQLException {
		if (ValueUtil.isNotEmpty(resultPower)) {
			double resultMath;
			String result = resultPower[3] == null ? resultPower[8] : resultPower[3];
			String resultValue = resultPower[5] == null ? resultPower[10] : resultPower[5];
			String[] operandos = FormulaCalculoSqlDbxDao.getInstance().getOperandos(resultValue.split(","));
			try {
				resultMath = Math.pow(ValueUtil.getDoubleValue(operandos[0]), ValueUtil.getDoubleValue(operandos[1]));
				if (Double.isInfinite(resultMath)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CONVERSAO_VALOR_INFINITO, new String[] {resultPower[3], operandos[0], operandos[1]}));
				}
			} catch (Exception e) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CONVERSAO_VALOR_POWER, new String[] {resultPower[3], operandos[0], operandos[1]})); 
			}
			int idxPower = sql.indexOf(result);
			StringBuffer stb = new StringBuffer();
			stb.append(sql.substring(0, idxPower));
			stb.append(resultMath + "");
			stb.append(sql.substring(idxPower + result.length(), sql.length()));
			return stb.toString();
		}
		return sql;
	}
	
	public String resolveText(String sql, String[] resultText) throws SQLException {
		if (ValueUtil.isNotEmpty(resultText)) {
			int idxText = sql.indexOf(resultText[0]);
			StringBuffer stb = new StringBuffer();
			stb.append(sql.substring(0, idxText));
			stb.append("'" + resultText[2] + "'");
			stb.append(sql.substring(idxText + resultText[0].length(), sql.length()));
			return stb.toString();
		}
		return sql;
	}
	
	private void populateDomainWithHashValues(BaseDomain domain, HashMap<String, Object> variaveisToSave) {
    	if (ValueUtil.isEmpty(variaveisToSave)) return;
    	String className = domain.getClass().getSimpleName();
    	for (String key : variaveisToSave.keySet()) {
    		if (!key.contains(className)) continue;
    		DomainUtil.set(domain, key.substring(className.length()+1), variaveisToSave.get(key));
		}
    }
	
}