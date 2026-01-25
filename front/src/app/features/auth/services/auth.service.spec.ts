import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('UserService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });

    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register a user', () => {
    // ARRANGE
    const registerRequest: RegisterRequest = {
        email: 'user@user.com',
        firstName: 'user',
        lastName: 'user',
        password: 'password'
    }

    const mockResponse = "User registered successfully!";

    // ACT
    service.register(registerRequest).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    })

    // ASSERT
    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest);
    req.flush(mockResponse);

  });

  it('Should login a user', () => {
    //ARRANGE
    const loginRequest: LoginRequest = {
      email: 'user@user.com',
      password: 'password'
    }

    const mockSessionInformation: SessionInformation = {
        token: 'montoken',
        type: 'Bearer',
        id: 1,
        username: 'user@user.com',
        firstName: 'user',
        lastName: 'user',
        admin: false
    }

    //ACT
    service.login(loginRequest).subscribe((sessionInformation) => {
      expect(sessionInformation).toEqual(mockSessionInformation);
    })

    //ASSERT
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBe(loginRequest);
    req.flush(mockSessionInformation);
  })

});

/*public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<SessionInformation> {
    return this.httpClient.post<SessionInformation>(`${this.pathService}/login`, loginRequest);
  }*/