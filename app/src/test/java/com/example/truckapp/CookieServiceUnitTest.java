package com.example.truckapp;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.services.cookie.CookieTypes;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CookieServiceUnitTest {

    @Mock
    Context contextMock;

    private CookieService cookieService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cookieService = new CookieService("testCookieService", ServicesController.getInstance(), true);
        cookieService.setContext(contextMock);
    }

    @Test
    public void getUserSession_shouldReturnNullWhenPreferencesDoNotContainUserSession() {
        // Arrange
        SharedPreferences preferencesMock = getMockedSharedPreferences();
        when(contextMock.getSharedPreferences(CookieTypes.SESSION, MODE_PRIVATE)).thenReturn(preferencesMock);

        // Act
        User userSession = cookieService.getUserSession();

        // Assert
        assertNull(userSession);
    }

    @Test
    public void getUserSession_shouldReturnUserObjectWhenPreferencesContainUserSession() {
        // Arrange
        User expectedUser = new User("testUsername", "testPassword", "testFullName", "123456789", 1);
        String jsonString = "{\"username\":\"testUsername\",\"password\":\"testPassword\",\"fullName\":\"testFullName\",\"phoneNumber\":\"123456789\",\"roleId\":1}";
        SharedPreferences preferencesMock = getMockedSharedPreferences(CookieTypes.SESSION, jsonString);
        when(contextMock.getSharedPreferences(CookieTypes.SESSION, MODE_PRIVATE)).thenReturn(preferencesMock);

        // Act
        User userSession = cookieService.getUserSession();

        // Assert
        assertNotNull(userSession);
        assertEquals(expectedUser.getUsername(), userSession.getUsername());
        assertEquals(expectedUser.getPassword(), userSession.getPassword());
        assertEquals(expectedUser.getFullName(), userSession.getFullName());
        assertEquals(expectedUser.getPhoneNumber(), userSession.getPhoneNumber());
        assertEquals(expectedUser.getRoleId(), userSession.getRoleId());
    }

    // TODO: Identify the bug in the following test
    @Test
    public void addSession_shouldSaveUserObjectToSharedPreferences() {
        // Arrange
        User user = new User("testUsername", "testPassword", "testFullName", "123456789", 1);
        SharedPreferences.Editor editorMock = getMockedSharedPreferencesEditor();
        when(contextMock.getSharedPreferences("test", MODE_PRIVATE)).thenReturn(null);

        // Act
        cookieService.addSession(user);

        // Assert
        Gson gson = new Gson();
        String expectedJsonString = gson.toJson(user);
        assertEquals(expectedJsonString, getMockedSharedPreferences().getString("data", null));
    }

    private SharedPreferences getMockedSharedPreferences() {
        return getMockedSharedPreferences(CookieTypes.SESSION, "");
    }

    private SharedPreferences getMockedSharedPreferences(String name, String jsonString) {
        SharedPreferences preferencesMock = org.mockito.Mockito.mock(SharedPreferences.class);
        when(preferencesMock.contains(CookieTypes.SESSION)).thenReturn(!jsonString.isEmpty());
        when(preferencesMock.getString("data", "")).thenReturn(jsonString);
        return preferencesMock;
    }

    private SharedPreferences.Editor getMockedSharedPreferencesEditor() {
        SharedPreferences.Editor editorMock = org.mockito.Mockito.mock(SharedPreferences.Editor.class);
        when(editorMock.putString(any(String.class), any(String.class))).thenReturn(editorMock);
        return editorMock;
    }
}