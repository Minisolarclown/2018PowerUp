package org.usfirst.frc.team7160.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Robot extends IterativeRobot {

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(0);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(0);
	WPI_TalonSRX backLeft = new WPI_TalonSRX(0);
	WPI_TalonSRX backRight = new WPI_TalonSRX(0);
	// For normal tank drive-
	/*
	 * SpeedControllerGroup left = new SpeedControllerGroup(frontLeft, backLeft);
	 * SpeedControllerGroup right = new SpeedControllerGroup(frontRight, backRight);
	 */
	// For our drive controls
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
		// Might have to do this
		backLeft.setInverted(true);
		frontLeft.setInverted(true);
		compressor.start();

	}

	@Override
	public void autonomousInit() {
		side = fms.getGameSpecificMessage();
		char outSwitch = side.charAt(0);
		char ourScale = side.charAt(1);
		char theirSwitch = side.charAt(2);
		position = fms.getLocation();
		timer.start();
	}

	@Override
	public void autonomousPeriodic() {
		if (timer.get() <= 5) {
			mainDrive.driveCartesian(0.5, 0, 0);
		} else {
			mainDrive.driveCartesian(0, 0, 0);
		}
	}

	@Override
	public void teleopPeriodic() {
		double x = joy1.getRawAxis(0);
		double y = joy1.getRawAxis(1);
		// Only if we use MacanumDrive
		double rot = joy1.getRawAxis(4);
		if (Math.abs(x) >= 0.05 || Math.abs(y) >= 0.05 || Math.abs(rot) >= 0.05) {
			mainDrive.driveCartesian(y, x, rot);
		} /*
			 * else {
			 * 
			 * if (joy1.getRawButton(1)) { mainDrive.driveCartesian(-0.5, 0, 0); } if
			 * (joy1.getRawButton(2)) { mainDrive.driveCartesian(0, 0.5, 0); } if
			 * (joy1.getRawButton(3)) { mainDrive.driveCartesian(0, -0.5, 0); } if
			 * (joy1.getRawButton(4)) { mainDrive.driveCartesian(0.5, 0, 0); } }
			 */
		if (joy1.getRawButton(5)) {
			clamp.set(DoubleSolenoid.Value.kForward);
		} else {
			clamp.set(DoubleSolenoid.Value.kReverse);
		}
	}

	@Override
	public void testPeriodic() {
	}
}
