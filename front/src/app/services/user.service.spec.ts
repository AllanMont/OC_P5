import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { User } from '../interfaces/user.interface';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  const mockUsers : User[] = [
    {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@gmail.com',
      password: '1234',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    },]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
  });

  it('should be get User by ID', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUsers[0]);
    });
  });

  it('should be delete User by ID', () => {
    service.delete('1').subscribe(user => {
      expect(user).toEqual(mockUsers[0]);
    });

    service.getById('1').subscribe(user => {
      expect(user).toBeUndefined();
    });
  });
});
