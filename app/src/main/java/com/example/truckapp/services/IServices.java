package com.example.truckapp.services;

import java.sql.SQLException;

/**
 * This interface represents a service that can be started and stopped, and that has a name.
 * Any class that implements this interface must have a constructor that takes three parameters:
 * a String representing the service name, a String representing the class name, and a boolean indicating
 * whether to use the logging service. For example:
 *
 * <pre>{@code
 * public class MyService implements IServices {
 *     public MyService(String serviceName, boolean useLoggingService) {
 *         // ...
 *     }
 *     // ...
 * }
 * }</pre>
 */
public interface IServices {
    default int start() throws SQLException {
        return 0;
    }

    default int stop() {
        return 0;
    }

    String getServiceName();
}
