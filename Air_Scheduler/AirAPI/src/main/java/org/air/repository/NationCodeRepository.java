package org.air.repository;

import org.air.entity.NationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface NationCodeRepository extends JpaRepository<NationCode, Long> {

    List<NationCode> findByCountryIn(List<String> country);

}
