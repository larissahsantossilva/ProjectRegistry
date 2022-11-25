package br.com.registry.larissahregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.registry.larissahregistry.entity.Registry;

@Repository
public interface RegistryRepository extends JpaRepository<Registry, Long> {
    
}
