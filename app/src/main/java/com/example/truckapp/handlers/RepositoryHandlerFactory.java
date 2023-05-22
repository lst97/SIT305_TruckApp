package com.example.truckapp.handlers;

public interface RepositoryHandlerFactory<T> {
    T getRepository(String repositoryName);

    void addRepository(String repositoryName, T repository);
}
