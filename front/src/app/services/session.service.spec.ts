import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be connected', () => {
    const user = {
      token: 'Plop1234',
      type: 'test_user_type',
      username: 'Allan34',
      firstName: 'Allan',
      lastName: 'Mont',
      id: 1,
      admin: false,
    };
    
    service.logIn(user);
    service.sessionInformation = user;
    
    expect(service.isLogged).toBe(true);
    });

  it('should be disconnected', () => {
    service.logOut();
    const user = {
      token: 'Plop1234',
      type: 'test_user_type',
      username: 'Allan34',
      firstName: 'Allan',
      lastName: 'Mont',
      id: 1,
      admin: false,
    };
    
    service.logIn(user);
    service.logOut();

    expect(service.isLogged).toBe(false);
  });

  it('should be observable', () => {
      expect(service.$isLogged).toBeTruthy();
  })
});
