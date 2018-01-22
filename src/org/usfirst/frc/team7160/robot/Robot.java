package org.usfirst.frc.team7160.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Robot extends IterativeRobot {

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(4);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(1);
	WPI_TalonSRX backLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX backRight = new WPI_TalonSRX(3);
	// For normal tank drive-
	//SpeedControllerGroup left = new SpeedControllerGroup(frontLeft, backLeft);
	//SpeedControllerGroup right = new SpeedControllerGroup(frontRight, backRight);
	MecanumDrive mainDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

	Joystick joy1 = new Joystick(0);

	Timer timer = new Timer();
	DriverStation fms = DriverStation.getInstance();

	DoubleSolenoid clamp = new DoubleSolenoid(0, 1);
	Compressor compressor = new Compressor();

	String side;
	int position;

	@Override
	public void robotInit() {
		compressor.start();
		mainDrive.setSafetyEnabled(false);

	}

	@Override
	public void autonomousInit() {
		side = fms.getGameSpecificMessage();
		char outSwitch = side.charAt(0);
		char ourScale = side.charAt(1);
		char theirSwitch = side.charAt(2);
		position = fms.getLocation();
		
		
	}

	@Override
	public void autonomousPeriodic() {
		timer.start();
		double autonTime = timer.get();
		while(autonTime <= 15.0) {
			mainDrive.driveCartesian(0.5, 0, 0);
		}
	}

	@Override
	public void teleopPeriodic() {
		double x = joy1.getRawAxis(1);
		double y = joy1.getRawAxis(0);
		double rot = joy1.getRawAxis(2);
		double speed = joy1.getRawAxis(3);
		// This will be how we change speeds.
		/*if(speed >= 0.5) {
			x = x/2;
			y = y/2;
			rot = rot/2;
		}*/
		if (y >= 0.05 || Math.abs(x) >= 0.05 || Math.abs(rot) >= 0.05) {
			mainDrive.driveCartesian(y / 2, x / 2, rot / 2);
		} else if (y >= -0.05 || Math.abs(x) >= 0.05 || Math.abs(rot) >= 0.05) {
			mainDrive.driveCartesian(-y / 4, x / 4, rot / 4);
		} else {
			mainDrive.driveCartesian(0, 0, 0);
		}
		
	}

	@Override
	public void testPeriodic() {
	}
}
