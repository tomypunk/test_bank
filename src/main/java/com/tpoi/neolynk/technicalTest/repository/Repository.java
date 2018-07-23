package com.tpoi.neolynk.technicalTest.repository;

import com.tpoi.neolynk.technicalTest.entity.AbstractEntity;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class Repository<T extends AbstractEntity>
{
    private final List<T> inMemoryRepository = new ArrayList<>();

    public void save(Optional<T> optionalEntity) throws RepositoryException
    {
        T entity = optionalEntity.orElseThrow(() -> new RepositoryException("Entity is null"));

        List<T> elementToDelete = new ArrayList<>(inMemoryRepository.size());
        for(T currentEntity : inMemoryRepository)
        {
            if(currentEntity.getId().equals(entity.getId()))
            {
                elementToDelete.add(currentEntity);
            }
        }
        inMemoryRepository.removeAll(elementToDelete);
        inMemoryRepository.add(entity);
    }

    public Collection<T> findAll()
    {
        return inMemoryRepository;
    }

    public void clear()
    {
        inMemoryRepository.clear();
    }

    public void removeEntity(Optional<T> entity) throws RepositoryException
    {
        inMemoryRepository.remove(entity.orElseThrow(() -> new RepositoryException("Entity is null")));
    }

    public Optional<T> findById(UUID uuid)
    {
        return inMemoryRepository.stream().filter(t -> t.getId().equals(uuid)).findFirst();
    }
}
