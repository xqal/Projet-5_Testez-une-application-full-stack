import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';


describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {

    // ARRANGE 
    const mockUser: User = { 
      id: 1,
      email: 'user@user.com',
      lastName: 'User',
      firstName: 'User',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    //ACT
    service.getById('1').subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    // ASSERT
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  })

  it('should delete user by id', () => {
    //ACT
    service.delete('1').subscribe((response) => {
      expect(response).toBeNull();
    })
    // ASSERT
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  })
});