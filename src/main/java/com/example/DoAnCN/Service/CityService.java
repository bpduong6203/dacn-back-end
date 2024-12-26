package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.CityDTO;
import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.Entity.City;
import com.example.DoAnCN.Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public City saveCity(City city){
        return cityRepository.save(city);
    }

    public Optional<City> updateCity(Long id){
        return cityRepository.findById(id);
    }

    public Optional<CityDTO> findById(Long id){
        return cityRepository.findById(id).map(dtoConverter::convertCityDTO);
    }

    public void deleteCity(Long id){
        cityRepository.deleteById(id);
    }

    public List<CityDTO> findAll(){
        return cityRepository.findAll().stream().map(dtoConverter::convertCityDTO).collect(Collectors.toList());
    }
}
