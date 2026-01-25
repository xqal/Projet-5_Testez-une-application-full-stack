import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { Teacher } from '../../../../interfaces/teacher.interface';
import { Session } from '../../interfaces/session.interface';

import { DetailComponent } from './detail.component';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let httpTestingController: HttpTestingController;
  let router: Router;


  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [ 
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: { snapshot: {paramMap: { get: (key: string) => '1' }} } }
      ]
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    

    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);

  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call participate and fetch session', () => {
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation();

    fixture.detectChanges();
    component.userId = '1';
    component.participate();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('POST');
    req.flush(null);
    
    expect(fetchSessionSpy).toHaveBeenCalled();
  });

  it('should call unparticipate and fetch session', () => {
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession').mockImplementation();

    fixture.detectChanges();
    component.userId = '1';
    component.unParticipate();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    
    expect(fetchSessionSpy).toHaveBeenCalled();
  });
  it('should fetch session and teacher details on initialization', () => {
    const mockSession: Session = {
      id: 1,
      name: 'test Session',
      description: 'Description',
      teacher_id: 5,
      users: [1, 2],
      date: new Date(),
    };

    const mockTeacher: Teacher = {
      id: 5,
      firstName: 'Margot',
      lastName: 'Delahaye',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    
    fixture.detectChanges(); 

    const sessionReq = httpTestingController.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush(mockSession);

    const teacherReq = httpTestingController.expectOne('api/teacher/5');
    expect(teacherReq.request.method).toBe('GET');
    teacherReq.flush(mockTeacher);

    expect(component.session).toBeDefined();
    expect(component.teacher).toBeDefined();
    expect(component.teacher?.firstName).toBe('Margot');

    expect(component.isParticipate).toBe(true); 
  });
});

