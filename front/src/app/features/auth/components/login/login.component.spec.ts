import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { LoginComponent } from './login.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authService: AuthService;
  let sessionService: SessionService;
  let httpTestingController: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    httpTestingController = TestBed.inject(HttpTestingController);


    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login', () => {
    const mockSessionInfo = { token: 'jwt', id: 1, admin: true } as any;

    const sessionSpy = jest.spyOn(sessionService, 'logIn').mockImplementation();
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation();

    component.form.setValue({
      email: 'test@test.com',
      password: 'test'
    });

    component.submit();

    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockSessionInfo);

  
    expect(sessionSpy).toHaveBeenCalledWith(mockSessionInfo);
    expect(routerSpy).toHaveBeenCalled();
  })

  it('should set onError to true when login fails', () => {
    component.onError = false;

    component.form.setValue({
      email: 'test@test.com',
      password: 'test'
    });

    component.submit();

    const req = httpTestingController.expectOne('api/auth/login');
    req.flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(component.onError).toBe(true);
    
    const sessionSpy = jest.spyOn(sessionService, 'logIn');
    expect(sessionSpy).not.toHaveBeenCalled();
  });

});
