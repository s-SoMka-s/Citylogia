using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace Core.Db.Migrations
{
    public partial class Initial : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.EnsureSchema(
                name: "citylogia");

            migrationBuilder.CreateTable(
                name: "Places-Types",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "text", nullable: true),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Places-Types", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "text", nullable: true),
                    Surname = table.Column<string>(type: "text", nullable: true),
                    Email = table.Column<string>(type: "text", nullable: true),
                    Password = table.Column<string>(type: "text", nullable: true),
                    PhotoId = table.Column<long>(type: "bigint", nullable: true),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Places",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    IsApproved = table.Column<bool>(type: "boolean", nullable: false),
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    Mark = table.Column<long>(type: "bigint", nullable: false),
                    Name = table.Column<string>(type: "text", nullable: true),
                    ShortDescription = table.Column<string>(type: "text", nullable: true),
                    Description = table.Column<string>(type: "text", nullable: true),
                    TypeId = table.Column<long>(type: "bigint", nullable: false),
                    Latitude = table.Column<double>(type: "double precision", nullable: false),
                    Longitude = table.Column<double>(type: "double precision", nullable: false),
                    City = table.Column<string>(type: "text", nullable: true),
                    Street = table.Column<string>(type: "text", nullable: true),
                    House = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Places", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Places_Places-Types_TypeId",
                        column: x => x.TypeId,
                        principalSchema: "citylogia",
                        principalTable: "Places-Types",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Places_Users_UserId",
                        column: x => x.UserId,
                        principalSchema: "citylogia",
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Favorite-Place-Links",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    PlaceId = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Favorite-Place-Links", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Favorite-Place-Links_Places_PlaceId",
                        column: x => x.PlaceId,
                        principalSchema: "citylogia",
                        principalTable: "Places",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Favorite-Place-Links_Users_UserId",
                        column: x => x.UserId,
                        principalSchema: "citylogia",
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Photos",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    PublicUrl = table.Column<string>(type: "text", nullable: true),
                    PlaceId = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Photos", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Photos_Places_PlaceId",
                        column: x => x.PlaceId,
                        principalSchema: "citylogia",
                        principalTable: "Places",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Reviews",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    IsApproved = table.Column<bool>(type: "boolean", nullable: false),
                    Body = table.Column<string>(type: "text", nullable: true),
                    Mark = table.Column<long>(type: "bigint", nullable: false),
                    PublishedAt = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false),
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    PlaceId = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Reviews", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Reviews_Places_PlaceId",
                        column: x => x.PlaceId,
                        principalSchema: "citylogia",
                        principalTable: "Places",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Reviews_Users_UserId",
                        column: x => x.UserId,
                        principalSchema: "citylogia",
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Favorite-Place-Links_PlaceId",
                schema: "citylogia",
                table: "Favorite-Place-Links",
                column: "PlaceId");

            migrationBuilder.CreateIndex(
                name: "IX_Favorite-Place-Links_UserId",
                schema: "citylogia",
                table: "Favorite-Place-Links",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Photos_PlaceId",
                schema: "citylogia",
                table: "Photos",
                column: "PlaceId");

            migrationBuilder.CreateIndex(
                name: "IX_Places_TypeId",
                schema: "citylogia",
                table: "Places",
                column: "TypeId");

            migrationBuilder.CreateIndex(
                name: "IX_Places_UserId",
                schema: "citylogia",
                table: "Places",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_PlaceId",
                schema: "citylogia",
                table: "Reviews",
                column: "PlaceId");

            migrationBuilder.CreateIndex(
                name: "IX_Reviews_UserId",
                schema: "citylogia",
                table: "Reviews",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Users_PhotoId",
                schema: "citylogia",
                table: "Users",
                column: "PhotoId",
                unique: true);

            migrationBuilder.AddForeignKey(
                name: "FK_Users_Photos_PhotoId",
                schema: "citylogia",
                table: "Users",
                column: "PhotoId",
                principalSchema: "citylogia",
                principalTable: "Photos",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Photos_Places_PlaceId",
                schema: "citylogia",
                table: "Photos");

            migrationBuilder.DropTable(
                name: "Favorite-Place-Links",
                schema: "citylogia");

            migrationBuilder.DropTable(
                name: "Reviews",
                schema: "citylogia");

            migrationBuilder.DropTable(
                name: "Places",
                schema: "citylogia");

            migrationBuilder.DropTable(
                name: "Places-Types",
                schema: "citylogia");

            migrationBuilder.DropTable(
                name: "Users",
                schema: "citylogia");

            migrationBuilder.DropTable(
                name: "Photos",
                schema: "citylogia");
        }
    }
}
