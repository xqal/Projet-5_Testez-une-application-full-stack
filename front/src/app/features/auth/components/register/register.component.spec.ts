import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { SessionService } from 'src/app/services/session.service';
import { AuthService } from 'src/app/features/auth/services/auth.service';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';


describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;

  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [SessionService, AuthService],
      imports: [
        BrowserAnimationsModule,
        HttpClientTestingModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

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

  it('should register successfully', () => {
    const routerSpy = jest.spyOn(router, 'navigate');

    component.form.setValue({
      firstName: 'User',
      lastName: 'User',
      email: 'user@user.com',
      password: 'test'
    });

    component.submit();

    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush(null);

    expect(routerSpy).toHaveBeenCalledWith(['/login']);
    expect(component.form.valid).toBe(true);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true when login fails', () => {
    component.onError = false;

    component.submit();

    const req = httpTestingController.expectOne('api/auth/register');
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(component.onError).toBe(true);
  });
});
