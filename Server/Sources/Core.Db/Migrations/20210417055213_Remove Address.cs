using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace Core.Db.Migrations
{
    public partial class RemoveAddress : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Places_Address_AddressId",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.DropTable(
                name: "Address",
                schema: "citylogia");

            migrationBuilder.DropIndex(
                name: "IX_Places_AddressId",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.DropColumn(
                name: "AddressId",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.AddColumn<string>(
                name: "Address",
                schema: "citylogia",
                table: "Places",
                type: "text",
                nullable: true);

            migrationBuilder.AddColumn<double>(
                name: "Latitude",
                schema: "citylogia",
                table: "Places",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<double>(
                name: "Longitude",
                schema: "citylogia",
                table: "Places",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Address",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.DropColumn(
                name: "Latitude",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.DropColumn(
                name: "Longitude",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.AddColumn<long>(
                name: "AddressId",
                schema: "citylogia",
                table: "Places",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.CreateTable(
                name: "Address",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    City = table.Column<string>(type: "text", nullable: true),
                    Country = table.Column<string>(type: "text", nullable: true),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false),
                    District = table.Column<string>(type: "text", nullable: true),
                    Flat = table.Column<long>(type: "bigint", nullable: true),
                    House = table.Column<long>(type: "bigint", nullable: false),
                    Latitude = table.Column<double>(type: "double precision", nullable: false),
                    Longitude = table.Column<double>(type: "double precision", nullable: false),
                    Postcode = table.Column<long>(type: "bigint", nullable: false),
                    Province = table.Column<string>(type: "text", nullable: true),
                    Street = table.Column<string>(type: "text", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Address", x => x.Id);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Places_AddressId",
                schema: "citylogia",
                table: "Places",
                column: "AddressId",
                unique: true);

            migrationBuilder.AddForeignKey(
                name: "FK_Places_Address_AddressId",
                schema: "citylogia",
                table: "Places",
                column: "AddressId",
                principalSchema: "citylogia",
                principalTable: "Address",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
