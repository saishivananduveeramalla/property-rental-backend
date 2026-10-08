package com.propertyrental.service;

import com.propertyrental.entity.*;
import com.propertyrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializerService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println(">>> Initializing Property Rental System Sample Database Data...");

        // 1. Admin
        User admin = new User(
                "System Administrator",
                "admin@propertyrental.com",
                "+91 9876543210",
                passwordEncoder.encode("admin123"),
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        // 2. Owners (3)
        User owner1 = new User("Rajesh Sharma", "rajesh.owner@example.com", "+91 9811122233", passwordEncoder.encode("owner123"), Role.ROLE_OWNER);
        User owner2 = new User("Priya Nair", "priya.owner@example.com", "+91 9822233344", passwordEncoder.encode("owner123"), Role.ROLE_OWNER);
        User owner3 = new User("Vikram Malhotra", "vikram.owner@example.com", "+91 9833344455", passwordEncoder.encode("owner123"), Role.ROLE_OWNER);
        userRepository.saveAll(Arrays.asList(owner1, owner2, owner3));

        // 3. Tenants (5)
        User tenant1 = new User("Rahul Verma", "rahul.tenant@example.com", "+91 9911100011", passwordEncoder.encode("tenant123"), Role.ROLE_TENANT);
        User tenant2 = new User("Sneha Patel", "sneha.tenant@example.com", "+91 9922200022", passwordEncoder.encode("tenant123"), Role.ROLE_TENANT);
        User tenant3 = new User("Amit Kumar", "amit.tenant@example.com", "+91 9933300033", passwordEncoder.encode("tenant123"), Role.ROLE_TENANT);
        User tenant4 = new User("Ananya Das", "ananya.tenant@example.com", "+91 9944400044", passwordEncoder.encode("tenant123"), Role.ROLE_TENANT);
        User tenant5 = new User("Rohit Joshi", "rohit.tenant@example.com", "+91 9955500055", passwordEncoder.encode("tenant123"), Role.ROLE_TENANT);
        userRepository.saveAll(Arrays.asList(tenant1, tenant2, tenant3, tenant4, tenant5));

        // 4. Properties (12 high-quality properties in Hyderabad, Bangalore, Mumbai, Chennai, Delhi)
        Property p1 = new Property(
                "Luxury 3BHK Apartment in Hitec City",
                "Spacious premium 3-bedroom apartment with panoramic lake view, modern Italian modular kitchen, wooden flooring in master bedroom, and 2 designated covered parking slots.",
                PropertyType.Apartment,
                "Tower B, Cyber Heights, Hitec City",
                "Hyderabad",
                "Telangana",
                "500081",
                42000.0,
                3,
                3,
                1850.0,
                FurnishedStatus.Furnished,
                "Swimming Pool, Gym, 24/7 Security, Power Backup, Club House, High Speed WiFi",
                true,
                owner1
        );
        p1.addImage("https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=1000&q=80");
        p1.addImage("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1000&q=80");

        Property p2 = new Property(
                "Modern 2BHK Flat near Manyata Tech Park",
                "Well-ventilated 2-bedroom flat with east-facing balconies, piped gas, geysers in all bathrooms, and round-the-clock maintenance staff. 5 mins from Manyata Tech Park gate.",
                PropertyType.Apartment,
                "Hebbal Outer Ring Road, Near Manyata",
                "Bangalore",
                "Karnataka",
                "560045",
                28000.0,
                2,
                2,
                1200.0,
                FurnishedStatus.Semi_Furnished,
                "Lift, Covered Parking, Intercom, Children Play Area, Rainwater Harvesting",
                true,
                owner1
        );
        p2.addImage("https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1000&q=80");
        p2.addImage("https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=1000&q=80");

        Property p3 = new Property(
                "Sea View 2BHK Condo in Bandra West",
                "Boutique apartment overlooking the Arabian Sea in a prime Bandra neighborhood. Tastefully designed with curated art, split air conditioners, and premium fittings.",
                PropertyType.Apartment,
                "Carter Road, Bandra West",
                "Mumbai",
                "Maharashtra",
                "400050",
                75000.0,
                2,
                2,
                1100.0,
                FurnishedStatus.Furnished,
                "Sea Facing, Concierge, Valet Parking, Rooftop Lounge, High Speed Elevators",
                true,
                owner2
        );
        p3.addImage("https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1000&q=80");
        p3.addImage("https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1000&q=80");

        Property p4 = new Property(
                "Elegant 4BHK Independent Villa with Lawn",
                "Magnificent independent duplex villa with private front lawn, private terrace garden, separate servant quarter, and solar power backup.",
                PropertyType.Villa,
                "Palm Meadows, Whitefield",
                "Bangalore",
                "Karnataka",
                "560066",
                85000.0,
                4,
                4,
                3200.0,
                FurnishedStatus.Furnished,
                "Private Garden, Gated Community, Badminton Court, Tennis Court, CCTV",
                true,
                owner2
        );
        p4.addImage("https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1000&q=80");

        Property p5 = new Property(
                "Cozy Studio Apartment in South Delhi",
                "Chic designer studio with kitchenette, smart TV, queen size bed, work desk, and balcony garden. Walkable distance to metro station.",
                PropertyType.Studio,
                "Hauz Khas Enclave, South Delhi",
                "Delhi",
                "Delhi",
                "110016",
                22000.0,
                1,
                1,
                550.0,
                FurnishedStatus.Furnished,
                "Metro Connectivity, Air Conditioning, Microwave, Refrigerator, Wi-Fi",
                true,
                owner3
        );
        p5.addImage("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1000&q=80");

        Property p6 = new Property(
                "Spacious 3BHK Independent House in OMR",
                "Bright independent ground + 1 house close to IT corridor. Features broad verandah, covered car porch, borewell & municipal water supply.",
                PropertyType.House,
                "Thoraipakkam, Old Mahabalipuram Road",
                "Chennai",
                "Tamil Nadu",
                "600097",
                32000.0,
                3,
                3,
                1600.0,
                FurnishedStatus.Semi_Furnished,
                "Water Purification, Dedicated Car Porch, Solar Water Heater, Pet Friendly",
                true,
                owner3
        );
        p6.addImage("https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=1000&q=80");

        Property p7 = new Property(
                "Premium Executive PG Room for Men/Women",
                "Fully managed executive paying guest accommodation with 3 daily nutritious meals, daily housekeeping, 100 Mbps fiber internet, and biometric entry.",
                PropertyType.PG,
                "Gachibowli, Near Bio Diversity Park",
                "Hyderabad",
                "Telangana",
                "500032",
                12000.0,
                1,
                1,
                250.0,
                FurnishedStatus.Furnished,
                "3 Meals Included, Daily Housekeeping, Laundry Facility, Biometric Access, WiFi",
                true,
                owner1
        );
        p7.addImage("https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?auto=format&fit=crop&w=1000&q=80");

        Property p8 = new Property(
                "Corporate Office Space / Commercial Floor",
                "Plug-and-play furnished commercial office space with 40 workstations, 2 conference rooms with video conferencing, manager cabins, and cafeteria.",
                PropertyType.Commercial,
                "Sector 62, Commercial Zone",
                "Delhi",
                "Noida/Delhi NCR",
                "201309",
                95000.0,
                0,
                4,
                2800.0,
                FurnishedStatus.Furnished,
                "Server Room, Central AC, 100% DG Backup, Cafeteria, Fire Safety Compliant",
                true,
                owner2
        );
        p8.addImage("https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1000&q=80");

        Property p9 = new Property(
                "Budget 1BHK Flat near Andheri Station",
                "Affordable 1-bedroom flat in a quiet residential society with piped gas and round-the-clock water supply. 8 mins walk from Andheri Railway Station.",
                PropertyType.Apartment,
                "Old Nagardas Road, Andheri East",
                "Mumbai",
                "Maharashtra",
                "400069",
                26000.0,
                1,
                1,
                580.0,
                FurnishedStatus.Unfurnished,
                "Near Railway Station, Security, Piped Gas, 24/7 Water",
                true,
                owner3
        );
        p9.addImage("https://images.unsplash.com/photo-1502005229762-ee1b2b814a79?auto=format&fit=crop&w=1000&q=80");

        Property p10 = new Property(
                "Ultra-Luxury 3BHK Penthouse in Jubilee Hills",
                "High-end luxury penthouse with private plunge pool, panoramic city views, private elevator, automation lighting, and designer Italian bathrooms.",
                PropertyType.Apartment,
                "Road No 36, Jubilee Hills",
                "Hyderabad",
                "Telangana",
                "500033",
                90000.0,
                3,
                4,
                2900.0,
                FurnishedStatus.Furnished,
                "Private Plunge Pool, Smart Home Automation, Private Terrace, 3 Car Parking, Gym",
                true,
                owner1
        );
        p10.addImage("https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?auto=format&fit=crop&w=1000&q=80");

        Property p11 = new Property(
                "Peaceful 2BHK Independent Floor in Vasant Kunj",
                "Second floor with roof rights, park facing balcony, spacious modular kitchen with chimney, and wooden almirahs in all bedrooms.",
                PropertyType.House,
                "Sector B, Pocket 1, Vasant Kunj",
                "Delhi",
                "Delhi",
                "110070",
                38000.0,
                2,
                2,
                1350.0,
                FurnishedStatus.Semi_Furnished,
                "Park Facing, Roof Rights, Modular Kitchen, 24/7 Gated Security",
                true,
                owner2
        );
        p11.addImage("https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=1000&q=80");

        Property p12 = new Property(
                "Modern 3BHK High-Rise Flat in Adyar",
                "Prestigious seaside neighborhood flat with modular kitchen, vitrified tiles, club house with infinity pool, squash court, and yoga deck.",
                PropertyType.Apartment,
                "Sardar Patel Road, Adyar",
                "Chennai",
                "Tamil Nadu",
                "600020",
                48000.0,
                3,
                3,
                1700.0,
                FurnishedStatus.Furnished,
                "Infinity Pool, Squash Court, Yoga Deck, 2 Covered Car Parks, Security",
                false, // Rented
                owner3
        );
        p12.addImage("https://images.unsplash.com/photo-1570129477492-45c003edd2be?auto=format&fit=crop&w=1000&q=80");

        propertyRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12));

        // 5. Sample Bookings (Pending, Approved, Completed)
        // Booking 1: Completed & Paid for p12 (Adyar) by tenant1
        Booking b1 = new Booking(
                p12,
                tenant1,
                owner3,
                LocalDate.now().minusDays(20),
                LocalDate.now().plusMonths(11),
                48000.0,
                528000.0,
                BookingStatus.COMPLETED,
                "Family relocating for work in Chennai."
        );
        bookingRepository.save(b1);

        Payment pay1 = new Payment(
                b1,
                48000.0,
                PaymentStatus.PAID,
                "TXN-" + System.currentTimeMillis() + "-CHEN01",
                "NET_BANKING"
        );
        paymentRepository.save(pay1);

        // Booking 2: Approved for p1 by tenant2
        Booking b2 = new Booking(
                p1,
                tenant2,
                owner1,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusMonths(6),
                42000.0,
                252000.0,
                BookingStatus.APPROVED,
                "Software engineer moving to Hyderabad Hitec City."
        );
        bookingRepository.save(b2);

        // Booking 3: Pending for p3 by tenant3
        Booking b3 = new Booking(
                p3,
                tenant3,
                owner2,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusMonths(12),
                75000.0,
                900000.0,
                BookingStatus.PENDING,
                "Senior consultant moving to Mumbai office."
        );
        bookingRepository.save(b3);

        // 6. Sample Messages
        ContactMessage msg1 = new ContactMessage(
                tenant2,
                owner1,
                p1,
                "Hello Mr. Rajesh, is the car parking slot on basement 1 or 2? Also, can we arrange a quick visit this Saturday?"
        );
        msg1.setReply("Hi Sneha, the parking slot is on Basement 1 directly beside the elevator. Saturday 11 AM works perfectly for a visit.");
        msg1.setRepliedAt(LocalDateTime.now().minusHours(2));
        contactMessageRepository.save(msg1);

        ContactMessage msg2 = new ContactMessage(
                tenant3,
                owner2,
                p3,
                "Hello Priya, is the maintenance included in the monthly rent of ₹75,000?"
        );
        contactMessageRepository.save(msg2);

        // 7. Sample Notifications
        notificationRepository.save(new Notification(
                tenant1,
                "Payment of ₹48,000.00 successful! Rental for \"Modern 3BHK High-Rise Flat in Adyar\" is confirmed. Transaction ID: " + pay1.getTransactionId(),
                "PAYMENT"
        ));
        notificationRepository.save(new Notification(
                tenant2,
                "Good news! Your rental request for \"Luxury 3BHK Apartment in Hitec City\" has been APPROVED by the owner. Please proceed with payment to confirm.",
                "APPROVAL"
        ));
        notificationRepository.save(new Notification(
                owner1,
                "New rental request received from Sneha Patel for property: Luxury 3BHK Apartment in Hitec City",
                "REQUEST"
        ));
        notificationRepository.save(new Notification(
                owner2,
                "New rental request received from Amit Kumar for property: Sea View 2BHK Condo in Bandra West",
                "REQUEST"
        ));

        System.out.println(">>> Sample Database Data Initialization Completed Successfully!");
    }
}
