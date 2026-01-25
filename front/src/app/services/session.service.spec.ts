import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';


describe('SessionService Test', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in a user', () => {
    // ARRANGE
    const user: SessionInformation = { 
      token: 'test-token', 
      type: 'Bearer', 
      id: 1, 
      username: 'test@example.com', 
      firstName: 'Test', 
      lastName: 'User', 
      admin: false 
    };

    const isLoggedSpy = jest.spyOn((service as any).isLoggedSubject, 'next');

    // ACT
    service.logIn(user);
    
    // ASSERT
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);
    expect(isLoggedSpy).toHaveBeenCalledWith(true);
  });

  it('should log out a user', () => {
    // ARRANGE
    const user: SessionInformation = { 
      token: 'test-token', 
      type: 'Bearer', 
      id: 1, 
      username: 'test@example.com', 
      firstName: 'Test', 
      lastName: 'User', 
      admin: false 
    };
    const isLoggedSpy = jest.spyOn((service as any).isLoggedSubject, 'next');

    service.logIn(user);

    // ACT
    service.logOut();
    
    // ASSERT
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
    expect(isLoggedSpy).toHaveBeenCalledWith(false);
  });
});
