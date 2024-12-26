package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.ProvinceDTO;
import com.example.DoAnCN.Entity.Province;
import com.example.DoAnCN.Repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public Province saveProvince(Province province){
        return provinceRepository.save(province);
    }

    public Optional<Province> updateById(Long id){
        return provinceRepository.findById(id);
    }

    public Optional<ProvinceDTO> findById(Long id){
        return provinceRepository.findById(id).map(dtoConverter::convertProvinceToDTO);
    }

    public void deleteProvince(Long id){
        provinceRepository.deleteById(id);
    }

    public List<ProvinceDTO> findAll(){
        return provinceRepository.findAll().stream().map(dtoConverter::convertProvinceToDTO).collect(Collectors.toList());
    }
}
