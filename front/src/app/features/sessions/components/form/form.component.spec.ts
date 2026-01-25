import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute } from '@angular/router';


import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let httpTestingController: HttpTestingController;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to sessions if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;

    const routerSpy = jest.spyOn(router, 'navigate');
    
    component.ngOnInit();
    
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should update session information when update is true', () => {
    const mockSession = {
      id: 1,
      name: 'Test Session',
      date: new Date(),
      description: 'Test Description',
      teacher_id: 1,
      users: []
    };

    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');

    const route = TestBed.inject(ActivatedRoute);;
    jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue('1');
    
    component.ngOnInit();
    
    const req = httpTestingController.expectOne(`api/session/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
    

    expect(component.onUpdate).toBe(true);
    expect(component['id']).toBe('1');
    expect(component.sessionForm?.value.name).toBe('Test Session');
  });


  it('should call create on submit if onUpdate is false', () => {

    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation();
    const exitPageSpy = jest.spyOn(component as any, 'exitPage').mockImplementation();

    component.onUpdate = false;
    component.sessionForm?.setValue({
      name: 'session name',
      date: '2023-01-01',
      teacher_id: '1',
      description: 'session description'
    });

    component.submit();

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush({});

    expect(exitPageSpy).toHaveBeenCalled();
  });
});
