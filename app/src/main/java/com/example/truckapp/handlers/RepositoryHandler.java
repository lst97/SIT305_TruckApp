package com.example.truckapp.handlers;

import com.example.truckapp.persistence.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepositoryHandler implements RepositoryHandlerFactory<RepositoryFactory> {
    private static RepositoryHandler instance = null;
    private final List<RepositoryFactory> repositories = new ArrayList<>();

    private RepositoryHandler() {
    }

    public static RepositoryHandler getInstance() {
        if (instance == null) {
            instance = new RepositoryHandler();
        }
        return instance;
    }

    @Override
    public RepositoryFactory getRepository(String repositoryName) {
        for (RepositoryFactory repository : repositories) {
            if (Objects.equals(repository.getRepositoryName(), repositoryName)) {
                return repository;
            }
        }
        return null;
    }

    @Override
    public void addRepository(String repositoryName, RepositoryFactory repository) {
        repository.setRepositoryName(repositoryName);
        if (getRepository(repositoryName) == null) {
            repositories.add(repository);
        }
    }
}
