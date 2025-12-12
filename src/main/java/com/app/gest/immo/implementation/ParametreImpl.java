package com.app.gest.immo.implementation;

import com.app.gest.immo.dto.ParametreDTO;
import com.app.gest.immo.entities.Parametre;
import com.app.gest.immo.entities.TypeParametre;
import com.app.gest.immo.repository.IParametreRepository;
import com.app.gest.immo.repository.ITypeParamRepository;
import com.app.gest.immo.service.IParametre;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParametreImpl implements IParametre {

	private final IParametreRepository iParam;
	private final ITypeParamRepository iTyepeParam;

	public ParametreImpl(IParametreRepository iParam, ITypeParamRepository iTyepeParam) {
        this.iParam = iParam;
		//this.typeMapper = typeMapper;
        this.iTyepeParam = iTyepeParam;
    }

	@Override
	public Parametre save(ParametreDTO typeParamDTO) throws Exception {
		Parametre typeParam = toEntity(typeParamDTO);
		return iParam.save(typeParam);
	}

	@Override
	public Parametre update(Long id, ParametreDTO typeParamDTO) throws Exception {
		Optional<Parametre> typeParam = iParam.findById(id);
		if(typeParam.get()==null) {
			throw new Exception("Pas de Type Param correspondant à cet id" + " -" + id);
		}
		Parametre type = typeParam.get();
		type = toEntity(typeParamDTO);
		return iParam.saveAndFlush(type);
	}

	@Override
	public Set<ParametreDTO> list() throws Exception {
		List<Parametre> list = iParam.findAll();
		Set<ParametreDTO> setType = new HashSet<ParametreDTO>();
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (Parametre type : list){
			setType.add(toDTO(type));
		}
		return setType;
	}

	@Override
	public void delete(Parametre typeParam) throws Exception {
		iParam.delete(typeParam);
		
	}

	@Override
	public void deleteById(Long id) throws Exception {
		Optional<Parametre> typeParam = iParam.findById(id);
		if(typeParam.get()==null) {
			throw new Exception("Pas de Type Param correspondant à cet id" + " -" + id);
		}
		Parametre type = typeParam.get();
		iParam.delete(type);
		
	}

	@Override
	public void deleteByCode(String code) throws Exception {
		Optional<Parametre> optional = iParam.findParametreByCode(code);
		if(optional.isEmpty()){
			throw new Exception("JJJJ");
		}
		iParam.delete(optional.get());
	}

	@Override
	public ParametreDTO   findByCode(String code) throws Exception {
		Optional<Parametre> listTypeParam = iParam.findParametreByCode(code);
		if (listTypeParam==null || listTypeParam.isEmpty()) {
			return null;
		}
		Parametre type = listTypeParam.get();
		return toDTO(type);
	}

	@Override
	public Parametre updateByCode(String code, ParametreDTO tyParamDTO) throws Exception {
		Parametre type = iParam.findParametreByCode(code).get();
		if(type==null) {
			throw new Exception("Pas de Type Param correspondant à cet id" + " -" + code);
		}
		Parametre typ = toEntity(tyParamDTO);
		return iParam.saveAndFlush(typ);
	}

	@Override
	public List<ParametreDTO> getAlläramByTypeParam(String codeType) throws Exception {
		List<ParametreDTO> dtoList = new ArrayList<>();
		Optional<TypeParametre> typeParametre = iTyepeParam.findTypeParametreByCode(codeType);
		if(typeParametre==null || typeParametre.isEmpty()){
			return null;
		}
		TypeParametre type = typeParametre.get();
		List<Parametre> list = iParam.findByTypeParam(type);
		if(list!=null){
			for(Parametre param : list){
				dtoList.add(toDTO(param));
			}
		}else{
			return null;
		}
		return dtoList;
	}

	@Override
	public Parametre findParametreByCodeAndTypeParam(String code, String typeParam) throws Exception {
		Optional<Parametre> parametreList = iParam.findParametreByCode(code);
		Optional<TypeParametre>  typeParametres = iTyepeParam.findTypeParametreByCode(typeParam);

		if(parametreList==null || parametreList.isEmpty()){
			throw new Exception("0");
		}
		parametreList = iParam.findParametreByCodeAndTypeParam(code, typeParametres.get());
		return parametreList.get();
	}

	Parametre toEntity(ParametreDTO parametreDTO){
		Parametre param = new Parametre();
		TypeParametre typeParametre = iTyepeParam.findTypeParametreByCode(parametreDTO.getTypeParam()).get();
		param.setCode(parametreDTO.getCode());
		param.setTypeParam(typeParametre);
		param.setChampLibre1(parametreDTO.getChampLibre1());
		param.setChampLibre2(parametreDTO.getChampLibre2());
		param.setChampLibre3(parametreDTO.getChampLibre3());
		param.setChampLibre4(parametreDTO.getChampLibre4());
		param.setLibelle(parametreDTO.getLibelle());
		return param;
	}

	ParametreDTO toDTO(Parametre parametre){
		ParametreDTO param = new ParametreDTO();
		param.setCode(parametre.getCode());
		param.setTypeParam(parametre.getTypeParam().getCode());
		param.setChampLibre1(parametre.getChampLibre1());
		param.setChampLibre2(parametre.getChampLibre2());
		param.setChampLibre3(parametre.getChampLibre3());
		param.setChampLibre4(param.getChampLibre4());
		param.setLibelle(parametre.getLibelle());
		return param;
	}

}
